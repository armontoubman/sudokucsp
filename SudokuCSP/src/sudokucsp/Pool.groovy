/*
 * Threadpool
 */

package sudokucsp
    
import java.util.concurrent.*;

/**
 *
 * @author Armon Toubman, Torec Luik
 */
class Pool {
    ExecutorService pool;
    
    Pool()
    {
        Runtime runtime = Runtime.getRuntime();
        int nrOfProcessors = runtime.availableProcessors();
        pool = Executors.newFixedThreadPool(nrOfProcessors);
    }
    
    def defer = { c -> pool.submit(c as Callable) }
    
    Future<String> solve(String line) {
        Future<String> f = defer{ Sudoku s = new Sudoku(line); s = Solver.solve(s); s.toString() };
        return f;
    }
    
    void close() { pool.shutdown(); }
	
}

