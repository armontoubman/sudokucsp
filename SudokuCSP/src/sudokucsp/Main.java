/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sudokucsp;

import java.io.*;

/**
 *
 * @author Armon
 */
class Main {

    public static void main(String[] args)
    {
        new Main().start(args);
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
        int aantal = 1;
        int i = 0;
        try {
            BufferedReader in = new BufferedReader(new FileReader(args[0]));

            String line;
            while ((line = in.readLine()) != null)
            {
                if(line.length() == 81)
                {
                    System.out.println(i);

                    Sudoku sudoku = new Sudoku(line);
                    sudoku = Solver.solve(sudoku);
                    System.out.println(sudoku);

                    i++;
                    //if(i==aantal) break;
                }
            }

            in.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        /*input.readLines()[0..10].each{
            Sudoku sudoku = new Sudoku(it)
            Sudoku solution = Solver.solve(sudoku)
            println solution.toString()
        }*/

    }

}

