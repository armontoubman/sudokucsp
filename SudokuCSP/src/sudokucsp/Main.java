/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sudokucsp;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 *
 * @author Armon
 */
class Main {

    public static void main(String[] args)
    {
        //new Main().start(args);
        new Main().oude_start(args);
    }

    void start(String[] args)
    {

        //
        // Formaliteiten om te kijken of alles klopt
        //

        // kijken of er 2 command-line argumenten zijn
        if(args.length != 2)
        {
            System.out.println("Expecting 2 arguments: input output");
            System.exit(1);
        }
        
        File input = new File(args[0]);
        File output = new File(args[1]);

        // kijken of de input wel bestaat
        if(!input.exists())
        {
            System.out.println("Input file does not exist");
            System.exit(1);
        }

        // kijken of de output al bestaat
        if(output.exists())
        {
            System.out.println("Output file exists, deleting");
            output.delete();
        }



        //
        // Input lezen, solven, output schrijven
        //
        int aantal = 2;
        int i = 0;
        
        Solver.PRINT = false;
        
        BufferedReader in = null;
        BufferedWriter out = null;
        Pool pool = new Pool();
        ConcurrentHashMap<Integer, Future<String>> results = new ConcurrentHashMap<Integer, Future<String>>();
        try {
            in = new BufferedReader(new FileReader(args[0]));
            out = new BufferedWriter(new FileWriter(args[1]));

            String line;
            while ((line = in.readLine()) != null)
            {
                if(line.length() == 81)
                {
                    if(Solver.PRINT) System.out.println(i);

                    Future<String> temp = pool.solve(line);
                    results.put(i, temp);

                    i++;
                    //if(i==aantal) break;
                }
            }

            in.close();
            pool.close();
            
            String r = "";
            for(int j = 0; j<i; j++)
            {
                r = results.get(j).get();
                out.write(r);
                out.newLine();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        } finally {
            //Close the BufferedWriter
            try {
                if(out != null)
                {
                    out.flush();
                    out.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

    }
    
    void oude_start(String[] args)
    {

        //
        // Formaliteiten om te kijken of alles klopt
        //

        // kijken of er 2 command-line argumenten zijn
        if(args.length != 2)
        {
            System.out.println("Expecting 2 arguments: input output");
            System.exit(1);
        }
        
        File input = new File(args[0]);
        File output = new File(args[1]);

        // kijken of de input wel bestaat
        if(!input.exists())
        {
            System.out.println("Input file does not exist");
            System.exit(1);
        }

        // kijken of de output al bestaat
        if(output.exists())
        {
            System.out.println("Output file exists, deleting");
            output.delete();
        }



        //
        // Input lezen, solven, output schrijven
        //
        int aantal = 100;
        int i = 0;
        
        //Solver.PRINT = true;
        
        BufferedReader in = null;
        BufferedWriter out = null;
        Sudoku sudoku = new Sudoku();
        try {
            in = new BufferedReader(new FileReader(args[0]));
            out = new BufferedWriter(new FileWriter(args[1]));

            String line;
            while ((line = in.readLine()) != null)
            {
                if(line.length() == 81)
                {
                    if(Solver.PRINT) System.out.println(i);

                    sudoku.init(line);
                    sudoku = Solver.solve(sudoku);
                    /*System.out.println("end_revise: "+Solver.revise);
                    System.out.println("end_hSingle: "+Solver.hSingle);
                    System.out.println("end_nPair: "+Solver.nPair);
                    System.out.println("end_hPair: "+Solver.hPair);*/
                    if(Solver.PRINT) System.out.println(sudoku);
                    
                    out.write(sudoku.toString());
                    out.newLine();

                    i++;
                    //if(i==aantal) break;
                }
            }

            in.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        } finally {
            //Close the BufferedWriter
            try {
                if(out != null)
                {
                    out.flush();
                    out.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

    }

}

