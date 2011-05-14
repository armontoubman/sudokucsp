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

    /**
     *
     * Backtracking
     * @param assignment (in)complete sudoku assignment
     * @return compleet ingevulde sudoku assignment
     */
    /**
     * TODO add constraint propogation techniques etc. uit slides en hiero:
     * http://kti.mff.cuni.cz/~bartak/constraints/consistent.html
     * TODO http://www.learn-sudoku.com/basic-techniques.html
     * TODO http://www.brainbashers.com/sudokuhelp.asp
     */
    static def bt(Sudoku s, int depth)
    {

                /**
         * first eliminate impossible strategies
         * TODO maak hier AC-3 van ipv AC-2 (i.e. "while revise")
         *
         * procedure AC-3
         *  Q <- {(Vi,Vj) in arcs(G),i#j};
         *  while not Q empty
         *      select and delete any arc (Vk,Vm) from Q;
         *      if REVISE(Vk,Vm) then
         *          Q <- Q union {(Vi,Vk) such that (Vi,Vk) in arcs(G),i#k,i#m}
         *      endif
         *  endwhile
         * end AC-3
         *
         * Het punt van AC-3 vergeleken met gewone revise, is dat als je 1x
         * revise toepast, dat er dan nieuwe 'givens' bij komen eigenlijk.
         * Dus, dan moet je eigenlijk herhaal revisen tot er nix meer is
         * veranderd. Nou moet je niet elke variabele dubbel gaan revisen, dat
         * is inefficient. Enkel degene die affected zijn! (Dat is AC3 ipv AC2)
         * zie: http://kti.mff.cuni.cz/~bartak/constraints/consistent.html
         */

        def removing = true; //Initiate loop
        while(removing){ // While revise has removed values
            removing = s.revise() // revise (again)
        }

        // pak een variable die nog niet assigned is
        def variables = s.getNotAssignedVariables()
        // alles assigned? hopelijk klaar
        if(variables.size() == 0){
            if(s.isConsistent()){ // klaar!
                return s
            }
            else { // jammer, vol maar fout!
                return null
            }
        }
        // pak cellnr
        def c = variables[0][0]

        /**
         *  Werkelijke oplossing is:
         *  7     9     4     5     8     2     1     3     6
         *  2     6     8     9     3     1     7     4     5
         *  3     1     5     4     7     6     9     8     2
         *  6     8     9     7     1     5     3     2     4
         *  4     3     2     8     6     9     5     7     1
         *  1     5     7     2     4     3     8     6     9
         *  8     2     1     6     5     7     4     9     3
         *  9     4     3     1     2     8     6     5     7
         *  5     7     6     3     9     4     2     1     8
         */

        // voor alle waarden in het domein van x
        for(v in s.getCell(c))
        {
                def testcopy = new Sudoku(s)
                // assign x deze waarde
                testcopy.setCell(c, [v])

               
                // als de nieuwe assignment consistent is
                if(testcopy.isConsistent())
                {

                 def spaties = " "*depth
                println spaties+testcopy
                //println s.assignment //ff wat handiger bij t debuggen/improven
                
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

