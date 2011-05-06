/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sudokucsp

/**
 *
 * @author Armon
 */
class SudokuFactory {

    SudokuFactory()
    {

    }

    def textToAssignment(input)
    {
        // input: .94...13..............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8
        // resultaat: [11:1..9, 12:[9], 13:[4], etc.
        
        input = input.trim()

        def assignment = [:];

        int row = 1;
        int col = 1;
        for(i in input)
        {
            def values;

            def cell = row*10+col;

            if(i == ".")
            {
                values = 1..9;
            }
            else
            {
                values = [ Integer.parseInt(i) ];
            }

            assignment[(cell)] = values;

            col++
            if(col > 9)
            {
                col = 1
                row++
            }
        }

        return assignment;
    }

    def assignmentToText(input)
    {
        println input
        /*def sb = ""
        for(i in 1..9)
        {
            for(j in 1..9)
            {
                def v = input[i*10+j][0]
                sb = sb+v
            }
        }
        return sb*/
    }
	
}

