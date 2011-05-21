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
        
        HashMap<String, Double> results = new HashMap<String, Double>();
        
        ArrayList<String> training = readSudokus("sudoku_training.txt");
        ArrayList<String> top95 = readSudokus("top95.txt");
        
        String test = "";
        
        //
        
        test = "rv";
        System.out.print(test+" ");
        results.put("training_"+test, go(training));
        results.put("top95_"+test, go(top95));
        System.out.println("Done");
        
        //
        Solver.HIDDENSINGLES = true;
        test = "rv_hs";
        System.out.print(test+" ");
        results.put("training_"+test, go(training));
        results.put("top95_"+test, go(top95));
        System.out.println("Done");
        
        //
        Solver.HIDDENSINGLES = false;
        Solver.NAKEDPAIRS = true;
        test = "rv_np";
        System.out.print(test+" ");
        results.put("training_"+test, go(training));
        results.put("top95_"+test, go(top95));
        System.out.println("Done");
        
        //
        Solver.NAKEDPAIRS = false;
        Solver.HIDDENPAIRS = true;
        test = "rv_hp";
        System.out.print(test+" ");
        results.put("training_"+test, go(training));
        results.put("top95_"+test, go(top95));
        System.out.println("Done");
        
        System.out.println(results);
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
        int t = 5;
        for(int i=0; i<t; i++)
        {
            mean += doTest(sudokus);
        }
        mean = mean/t;
        return mean; // seconden
    }

}

