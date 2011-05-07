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

    static def solve(Sudoku s)
    {
        return bt(s, 0)
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
    /* TODO optimizen! Hij doet nu al 70 minuten oid en nog geen sudoku :P
     * TODO add constraint propogation techniques etc. uit slides en hiero:
     * http://kti.mff.cuni.cz/~bartak/constraints/consistent.html
     * TODO slim v kiezen om eerst te branchen
    */
    static def bt(Sudoku s, int depth)
    {
        // pak een variable die nog niet assigned is
        def variables = s.getNotAssignedVariables()
        // alles assigned? klaar
        if(variables.size() == 0) return s
        // pak cellnr
        def c = variables[0]
        
        // voor alle waarden in het domein van x
        for(v in s.getCell(c))
        {
                def testcopy = new Sudoku(s)
                // assign x deze waarde
                testcopy.setCell(c, [v])
                
                def spaties = " "*depth
                println spaties+testcopy
                
                // als de nieuwe assignment consistent is
                if(testcopy.isConsistent())
                {
                    // maak nieuwe branch
                    def R = bt(testcopy, depth+1)
                    // als deze branch niet faalt
                    if(R != null)
                    {
                        return R
                    }
                }
        }
        return null
    }
    
}

