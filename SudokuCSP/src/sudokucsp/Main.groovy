/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sudokucsp

/**
 *
 * @author Armon
 */
class Main {

    public static void main(String[] args)
    {
        new Main().start(args)
    }

    // TODO kijken of multithreading kan
    // zie Fibonacci with Executors (Groovy documentatie)
    void start(String[] args)
    {

        //
        // Formaliteiten om te kijken of alles klopt
        //

        // kijken of er 2 command-line argumenten zijn
        if(args.size() != 2)
        {
            println "Expecting 2 arguments: input output"
            System.exit(1)
        }

        def input = new File(args[0])
        def output = new File(args[1])

        // kijken of de input wel bestaat
        if(!input.exists())
        {
            println "Input file does not exist"
            System.exit(1)
        }

        // kijken of de output al bestaat
        if(output.exists())
        {
            println "Output file exists, deleting"
            output.delete()
        }



        //
        // Input lezen, solven, output schrijven
        //

        // input lezen
        //input.eachLine{
        input.readLines()[0..10].each{
            Sudoku sudoku = new Sudoku(it)
            Sudoku solution = Solver.solve(sudoku)
            println solution.toString()
        }

    }

}

