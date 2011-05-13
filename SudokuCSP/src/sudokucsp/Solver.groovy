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
        // pak een variable die nog niet assigned is
        def variables = s.getNotAssignedVariables()
        // alles assigned? klaar
        if(variables.size() == 0) return s
        // pak cellnr
        def c = variables[0][0]

        /* TODO !!!!!!! Zorgen dat ie ook naar subgrids kijkt (ipv enkel row/col)
         *
         * TODO kijken of openSingles nut heeft
         * technique: http://www.learn-sudoku.com/open-singles.html
         * Fill in the last remaining number.
         * Probably very redundant and without actual performance boosting:
         *  seems to be inherent to revise already.
         * DOES NOT WORK W/ RECURSION, dus enkel in t begin
         * met recursie: hij gaat dingen aangeven voor voorgaande levels,
         * backtracking houdt dan dus de aannames bij van foute takken
        */
        //if(depth == 0) { s.openSingles() }

        /* met opensingles op level 0:
         *294...13..............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8
 2945..13..............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8
  29456.13..............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8
   29456713..............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8
  29458.13..............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8
   29458713..............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8
    294587136.............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8
     2945871361............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8

        vs

        zonder:
        294...13..............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8
 2945..13..............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8
  29456.13..............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8
   29456713..............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8
  29458.13..............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8
   29458713..............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8
    294587136.............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8
     2945871361............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8
         */


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
        def removing = true;
        while(removing){
            removing = s.revise(c)
        }
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

