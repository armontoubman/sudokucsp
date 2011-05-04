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

    // TODO uitvinden waarom ie niet werkt
    def bt(assignment)
    {
        def variables = getNotAssignedVariables(assignment)
        if(variables.size() == 0) return assignment
        def x = variables[0]
        assignment[x].each({
                def testcopy = assignment
                testcopy[x] = [ it ]
                if(consistent(testcopy))
                {
                    def R = bt(testcopy)
                    if(R != null)
                    {
                        return R
                    }
                }
        })
        return null
    }

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

    def consistent(s)
    {

        def assigned = s.findAll{ it.value.size() == 1 }
        return assigned.every{
            onlyAppearsOnceInRow(it.value, it.key, s) && onlyAppearsOnceInColumn(it.value, it.key, s)
        }
    }

    def onlyAppearsOnceInRow(x, cell, s)
    {
        def srow = s.findAll{ getRowFromCellNr(it.key) == getRowFromCellNr(cell) }
        def result = onlyAppearsOnceInRange(x, srow)
    }

    def onlyAppearsOnceInColumn(x, cell, s)
    {
        def scol = s.findAll{ getColFromCellNr(it.key) == getColFromCellNr(cell) }
        def result = onlyAppearsOnceInRange(x, scol)
    }

    def onlyAppearsOnceInRange(x, range)
    {
        def result = range.values().toList().count(x) <= 1
    }

    def getRowFromCellNr(i)
    {
        def row = Math.floor(i / 10)
    }

    def getColFromCellNr(i)
    {
         def col = i % 10
    }
}

