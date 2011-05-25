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
        
      /* gedaan
        //
        Solver.HIDDENSINGLES = true;
        Solver.HEURISTIC1 = true;
        test("rv_hs_h1");
        
        //
        Solver.HEURISTIC1 = false;
        Solver.HEURISTIC3 = true;
        test("rv_hs_h3");
        
        //
        Solver.HEURISTIC3 = false;
        Solver.HEURISTIC13 = true;
        test("rv_hs_h13");
      */
        // top95_rv_hs_h3=48.22889132033333, training_rv_hs_h1=29.734570621, top95_rv_hs_h1=49.521653201, top95_rv_hs_h13=51.78545695633333, training_rv_hs_h3=29.912008605, training_rv_hs_h13=29.857984153
        
      /* gedaan
        //
        test("rv");
        
        //
        Solver.HEURISTIC1 = true;
        test("rv_h1");
        
        //
        Solver.HEURISTIC1 = false;
        Solver.HEURISTIC3 = true;
        test("rv_h3");
        
        //
        Solver.HEURISTIC3 = false;
        Solver.HEURISTIC13 = true;
        test("rv_h13");
      */
        // {top95_rv=6924.5242909, training_rv_h13=540.58733985, top95_rv_h1=7023.125097676, top95_rv_h13=7011.893442942, training_rv_h1=532.882686773, training_rv_h3=543.003054551, training_rv=536.094290474, top95_rv_h3=7060.009184544}
        
      /* gedaan
        //
        Solver.HIDDENSINGLES = true;
        Solver.NAKEDPAIRS = true;
        Solver.HIDDENPAIRS = true;
        test("rv_hs_hp_np");
        
        //
        Solver.HEURISTIC1 = true;
        test("rv_hs_hp_np_h1");
        
        //
        Solver.HEURISTIC1 = false;
        Solver.HEURISTIC3 = true;
        test("rv_hs_hp_np_h3");
        
        //
        Solver.HEURISTIC3 = false;
        Solver.HEURISTIC13 = true;
        test("rv_hs_hp_np_h13");
      */
        // {top95_rv_hs_hp_np=9.685215524666667, top95_rv_hs_hp_np_h13=9.13048304, training_rv_hs_hp_np_h1=26.819459991, training_rv_hs_hp_np_h3=25.575965089666667, top95_rv_hs_hp_np_h1=9.618339162333333, top95_rv_hs_hp_np_h3=9.190885884666669, training_rv_hs_hp_np_h13=25.511620738333335, training_rv_hs_hp_np=26.050099973333335}
        
      /* gedaan
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
        
        //
        Solver.HIDDENPAIRS = false;
        Solver.HIDDENSINGLES = true;
        Solver.NAKEDPAIRS = true;
        test("rv_hs_np");
        
        //
        Solver.NAKEDPAIRS = false;
        Solver.HIDDENPAIRS = true;
        test("rv_hs_hp");
        
        //
        Solver.HIDDENSINGLES = false;
        Solver.NAKEDPAIRS = true;
        test("rv_hp_np");
      */
        //{top95_rv_hp_np=230.84322473, training_rv_hp=489.462033154, training_rv_hp_np=81.19639021366667, top95_rv_hp=1596.8818117199999, training_rv_hs_hp=27.667227801666666, training_rv_hs_np=23.296235251333332, top95_rv_hs_hp=15.692717729, top95_rv_hs_np=20.824697618666665, training_rv_np=76.90551903866667, training_rv_hs=29.234435128, top95_rv_np=1172.4098914453334, top95_rv_hs=47.415598335}
        
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

