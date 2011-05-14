/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sudokucsp;

import java.io.*;
import java.util.*;

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
        int aantal = 10;
        int i = 0;
        String line;
        try {
            BufferedReader in = new BufferedReader(new FileReader(args[0]));
            if (!in.ready())
                throw new IOException();

            while ((line = in.readLine()) != null && i < aantal)
            {
                i++;
                Sudoku sudoku = new Sudoku(line);
                sudoku = Solver.solve(sudoku);
                System.out.println(sudoku);
            }

            in.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        /*input.readLines()[0..10].each{
            Sudoku sudoku = new Sudoku(it)
            Sudoku solution = Solver.solve(sudoku)
            println solution.toString()
        }*/

    }

}

