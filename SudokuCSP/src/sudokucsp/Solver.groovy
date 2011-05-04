/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sudokucsp

/**
 *
 * @author Armon
 */
class Solver {

    Solver()
    {

    }

    def solve(problem)
    {
        return bt(problem)
    }

    /*
    procedure BT(X:variables, V:assignment, C:constraints)
        if X={} then return V
        x := select a not-yet assigned variable from X
        for each value h from the domain of x do
            if consistent(V+{x/h}, C) then
                R := BT(X-{x}, V+{x/h}, C)
            if R != fail then return R
        end for
        return fail
    end BT
    */

    /*
    * Backtracking
    * @param assignment (in)complete sudoku assignment
    * @return compleet ingevulde sudoku assignment
    */
    /* TODO testcopy local maken.
     * Momenteel onthoudt testcopy alle [it]s die
     * eraan toe zijn gevoegd wanneer
     * assignment.each een variabel verder gaat.
     * Hierdoor blijft de false waarde staan als
     * niets meer past in een vakje. ergo: alles false
     *
     * bewijs:
     * 11 = 2, 14 = 5, 15 = 6, 16 = 7 en dan 19 = 9
     * Dit is false, maar niets anders past meer in 9, dus:
     * 11 = 2, 14 = 5, 15 = 6, 16 = 8 en nog 19 = 9
     * Nog steeds false door 19, etc tot assignment = null
    */
    def bt(assignment)
    {
        // pak een variable die nog niet assigned is
        def variables = getNotAssignedVariables(assignment)
        // alles assigned? klaar
        if(variables.size() == 0) return assignment
        // pak cellnr
        def x = variables[0]
        // voor alle waarden in het domein van x
        for(v in assignment[x])
        {
                def testcopy = assignment.clone()
                // assign x deze waarde
                testcopy[x] = [ v ]
                
                println testcopy
                println ""
                
                // als de nieuwe assignment consistent is
                if(consistent(testcopy))
                {
                    // maak nieuwe branch
                    def R = bt(testcopy)
                    // als deze branch niet faalt
                    if(R != null)
                    {
                        return R
                    }
                }
        }
        // geen oplossing
        return null
    }

    /*
    * Geeft de eerste variabele die nog niet assigned is
    * oftewel een lijst (size > 1) met mogelijke waarden heeft
    * @param incomplete assignment
    * @return lijst met 1 element, cellnr
    */
    def getNotAssignedVariables(assignment)
    {
        def result = []
        for(pair in assignment)
        {
            if(pair.value.size() > 1)
            {
                result << pair.key
                break // stoppen zodra de eerste gevonden is, sneller
            }
        }
        return result
    }

    /*
    * Kijkt of een sudoku assignment consistent is:
    * elke waarde die assigned is mag maar 1x voorkomen in zijn rij en kolom
    * @param incomplete sudoku assignment
    * @return consistent of niet
    */
    def consistent(s)
    {
        // pak alle cells die assigned zijn
        def assigned = s.findAll{ it.value.size() == 1 }
        // voor elke assigned cell
        return assigned.every{
            // kijk of ie 1x voorkomt in zijn row en column
            onlyAppearsOnceInRow(it.value, it.key, s) && onlyAppearsOnceInColumn(it.value, it.key, s)
        }
    }

    /*
    * Kijkt of waarde x eenmaal voorkomt in de row van cell in assignment s
    * @param x waarde
    * @param cell cell
    * @param s sudoku assignment
    */
    def onlyAppearsOnceInRow(x, cell, s)
    {
        // pak alle cells in de sudoku in dezelfde rij als cell
        def srow = s.findAll{ getRowFromCellNr(it.key) == getRowFromCellNr(cell) }
        // controleer of x 1x voorkomt in deze cells
        def result = onlyAppearsOnceInRange(x, srow)
    }

    /*
    * Kijkt of waarde x eenmaal voorkomt in de column van cell in assignment s
    * @param x waarde
    * @param cell cell
    * @param s sudoku assignment
    */
    def onlyAppearsOnceInColumn(x, cell, s)
    {
        // pak alle cells in de sudoku in dezelfde kolom als cell
        def scol = s.findAll{ getColFromCellNr(it.key) == getColFromCellNr(cell) }
        // controleer of x 1x voorkomt in deze cells
        def result = onlyAppearsOnceInRange(x, scol)
    }

    /*
    * Kijkt of waarde x eenmaal of niet voorkomt in een groepje cells
    * x waarde
    * range groepje cells
    */
    def onlyAppearsOnceInRange(x, range)
    {
        def result = range.values().toList().count(x) <= 1
    }

    /*
    * Haalt het rownr uit een cellnr
    * i cellnr
    */
    def getRowFromCellNr(i)
    {
        def row = Math.floor(i / 10)
    }

    /*
    * Haalt het colnr uit een cellnr
    * i colnr
    */
    def getColFromCellNr(i)
    {
         def col = i % 10
    }
}

