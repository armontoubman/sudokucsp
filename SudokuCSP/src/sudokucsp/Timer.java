/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sudokucsp;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Armon
 */
class Timer {

    public static void main(String[] args)
    {
        new Timer().start(args);
    }
    
    HashMap<String, Double> results;
    
    ArrayList<String> training, top95;

    void start(String[] args)
    {
        Solver.REVISE = true;
        Solver.HIDDENSINGLES = false;
        Solver.NAKEDPAIRS = false;
        Solver.HIDDENPAIRS = false;
        
        Solver.ORDERVARIABLES = false;
        Solver.ORDERVALUES = false;

        Solver.HEURISTIC1 = false; // Nr of Children
        Solver.HEURISTIC13 = false; // Heuristic 1&3
        //static boolean HEURISTIC2 = false;
        Solver.HEURISTIC3 = false;
        
        results = new HashMap<String, Double>();
        
        training = readSudokus("sudoku_training.txt");
        top95 = readSudokus("top95.txt");
        
        //
        Solver.HIDDENSINGLES = true;
        test("rv_hs");
        
        //
        Solver.HIDDENSINGLES = false;
        Solver.NAKEDPAIRS = true;
        test("rv_np");
        
        //
        Solver.NAKEDPAIRS = false;
        Solver.HIDDENPAIRS = true;
        test("rv_hp");
    }
    
    ArrayList<String> readSudokus(String filename)
    {
        
        ArrayList<String> sudokus = new ArrayList<String>();
        
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filename));

            String line;
            while ((line = in.readLine()) != null)
            {
                if(line.length() == 81)
                {
                    sudokus.add(line);
                }
            }

            in.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return sudokus;
    }
    
    void solve(ArrayList<String> sudokus)
    {
        for(String line : sudokus)
        {
            Sudoku sudoku = new Sudoku(line);
            sudoku = Solver.solve(sudoku);
        }
    }
    
    double doTest(ArrayList<String> sudokus)
    {
        long start = System.nanoTime();
        solve(sudokus);
        return (double) (System.nanoTime() - start) / 1000000000.0;
    }
    
    double go(ArrayList<String> sudokus)
    {
        double mean = 0.0;
        int t = 3;
        for(int i=0; i<t; i++)
        {
            System.out.print(i);
            mean += doTest(sudokus);
        }
        System.out.print(" ");
        mean = mean/t;
        return mean; // seconden
    }
    
    void test(String name)
    {
        System.out.print(name+" ");
        results.put("training_"+name, go(training));
        results.put("top95_"+name, go(top95));
        System.out.println();
        System.out.println(results);
    }

}

