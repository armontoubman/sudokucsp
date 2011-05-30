/*
 * Multithreaded
 */

package sudokucsp;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 *
 * @author Armon Toubman, Torec Luik
 */
class Main {

    public static void main(String[] args)
    {
        new Main().start(args);
        //new Main().oude_start(args);
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

}

