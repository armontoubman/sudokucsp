/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sudokucsp

/**
 *
 * @author Armon
 */
class Sudoku {
    
    def assignment
    
    Sudoku(String input)
    {
        this.assignment = textToAssignment(input)
    }
    
    Sudoku(Sudoku s)
    {
        this.assignment = s.assignment
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
    
    String toString()
    {
        def sb = ""
        for(i in 1..9)
        {
            for(j in 1..9)
            {
                def x
                if(this.assignment[i*10+j].size() > 1)
                {
                    x = "."
                }
                else
                {
                    x = this.assignment[i*10+j][0]
                }
                sb = sb+x
            }
        }
        return sb
    }
    
    def getCell(c)
    {
        return this.assignment[c];
    }
    
    def setCell(c, vs)
    {
        this.assignment[c] = vs;
    }

    /*
    * Kijkt of de sudoku consistent is:
    * (1) elke waarde is uit 1..9
    * (2) elke waarde die assigned is mag maar 1x voorkomen in zijn rij en kolom
    *
    * @return consistent of niet
    */
    def isConsistent()
    {
        // (1)
        def one = this.assignment.every{
            [1,2,3,4,5,6,7,8,9].containsAll(it.value) && it.value.size() > 0
        }
        // (2)
        // pak alle cells die assigned zijn
        def assigned = this.assignment.findAll{ it.value.size() == 1 }
        // voor elke assigned cell
        def two = assigned.every{
            // kijk of ie 1x voorkomt in zijn row en column
            onlyAppearsOnceInRow(it.value, it.key) && onlyAppearsOnceInColumn(it.value, it.key)
        }

        return one && two
    }
    
    /*
    * Geeft de eerste variabele die nog niet assigned is
    * oftewel een lijst (size > 1) met mogelijke waarden heeft
    * @param incomplete assignment
    * @return lijst met 1 element, cellnr
    */
    def getNotAssignedVariables()
    {
        def result = []
        for(pair in this.assignment)
        {
            if(pair.value.size() > 1)
            {
                result << pair.key
                //break // stoppen zodra de eerste gevonden is, sneller
            }
        }
        return result
    }
    
    /*
     * Geeft een lijst met cells die assigned zijn en hun waarde
     * @param assignment (in)complete assignment
     * @return lijst met lijst per cel, bijv: [ [ 11, 1 ], [ 63, 5 ] ]
     */
    def getAssignedVariables()
    {
        def result = []
        for(pair in this.assignment)
        {
            if(pair.value.size() == 1)
            {
                result << [pair.key, pair.value]
            }
        }
        return result
    }

    /*
    * Kijkt of waarde x eenmaal voorkomt in de row van cell
    * @param v waarde
    * @param c cell
    */
    def onlyAppearsOnceInRow(v, c)
    {
        // pak alle cells in de sudoku in dezelfde rij als cell
        def srow = this.assignment.findAll{ getRowFromCellNr(it.key) == getRowFromCellNr(c) }
        // controleer of x 1x voorkomt in deze cells
        def result = onlyAppearsOnceInRange(v, srow)
    }

    /*
    * Kijkt of waarde x eenmaal voorkomt in de column van cell
    * @param v waarde
    * @param c cell
    */
    def onlyAppearsOnceInColumn(v, c)
    {
        // pak alle cells in de sudoku in dezelfde kolom als cell
        def scol = this.assignment.findAll{ getColFromCellNr(it.key) == getColFromCellNr(c) }
        // controleer of x 1x voorkomt in deze cells
        def result = onlyAppearsOnceInRange(v, scol)
    }

    /*
    * Kijkt of waarde x eenmaal of niet voorkomt in een groepje cells
    * v waarde
    * range groepje cells (deel van een Map)
    */
    def onlyAppearsOnceInRange(v, range)
    {
        def result = range.values().toList().count(v) <= 1
    }

    /*
    * Haalt het rownr uit een cellnr
    * c cellnr
    */
    def getRowFromCellNr(c)
    {
        def row = (int) Math.floor(c / 10)
    }

    /*
    * Haalt het colnr uit een cellnr
    * c cellnr
    */
    def getColFromCellNr(c)
    {
         def col = c % 10
    }
	
}

