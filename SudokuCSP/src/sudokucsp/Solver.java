/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sudokucsp;

import java.util.*;

/**
 *
 * @author Armon
 */
class Solver {
    
    static boolean REVISE = true;
    static boolean HIDDENSINGLES = true;
    static boolean NAKEDPAIRS = true;
    static boolean ORDERVARIABLES = true;
    static boolean ORDERVALUES = false;
    
    static boolean HIDDENPAIRS = false; // wordt slechter

    static boolean HEURISTIC1 = true;

    static Sudoku solve(Sudoku s)
    {
        return bt(s, 0);
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
     * TODO constraint propogation : http://kti.mff.cuni.cz/~bartak/constraints/consistent.html
     * TODO sudoku techniques : http://www.learn-sudoku.com/basic-techniques.html
     * TODO sudoku techniques : http://www.brainbashers.com/sudokuhelp.asp
     */
    static Sudoku bt(Sudoku s, int depth)
    {

                /**
         * first eliminate impossible strategies
         * TODO (NOT DONE) maak hier AC-3 van ipv AC-2 (dus ipv "while revise")
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

        boolean removing = true; //Initiate loop
        while(removing){ // While revise has removed values
            removing = s.revise(); // revise (again)
        }

        // pak een variable die nog niet assigned is
        HashMap<Integer, ArrayList<Integer>> variables = s.getNotAssignedVariables();
        // alles assigned? hopelijk klaar
        if(variables.isEmpty()){
            if(s.isConsistent()){ // klaar!
                return s;
            }
            else { // jammer, vol maar fout!
                return null;
            }
        }
        
        int c;
        if(ORDERVARIABLES)
        {
            c = orderVariables(variables);
        }
        else
        {
            // pak cellnr
            c = (int) new ArrayList<Integer>(variables.keySet()).get(0);
        }

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
        
        ArrayList<Integer> values = s.getCell(c);
        
        if(ORDERVALUES)
        {
            values = orderValues(values);
        }

        // voor alle waarden in het domein van x
        for(int v : values)
        {
                Sudoku testcopy = new Sudoku(s);
                // assign x deze waarde
                ArrayList<Integer> newValues = new ArrayList<Integer>();
                newValues.add(v);
                testcopy.setCell(c, newValues);

               
                // als de nieuwe assignment consistent is
                if(testcopy.isConsistent())
                {

                for(int i=0; i<depth; i++) { System.out.print(" "); }
                System.out.println(testcopy);
                //println s.assignment //ff wat handiger bij t debuggen/improven
                
                    // maak nieuwe branch
                    Sudoku R = bt(testcopy, depth+1);
                    // als deze branch niet faalt
                    if(R != null)
                    {
                        return R;
                    }
                }
        }
        return null;
    }
    
    /*
     * TODO depth-first --> best-first : add heuristics (reduce search space)
     * Heuristics : informed guess of the next step to be taken (domain dependent)
     * I.e. combine depth-first met wat slimme breadth-first
     * f(n) = g(n) + h(n) : g(n) = depth of node n, h(n) = heuristic value ?
     * Kunnen g(n) nog wel even overslaan, hoeven we geen score systeem te hebben
     * Mogelijke opties:
     *  H1: Kies Node eerder, hoe minder kinderen het heeft
     *  H2: Kies Node eerder, hoe meer zijn values al voorkomen in de Sudoku
     *  H3: Kies Node eerder, hoe meer constrained deze is (som values in row/col/reg)
     *  H4: Diepte constrainen, bvb via g(n). Misschien zijn andere nodes eerder opgelost.
     *  H5: ... andere maten die zouden kunnen aangeven of iets dichter bij solution komt.
     *
     */
    static int orderVariables(HashMap<Integer, ArrayList<Integer>> init){
        int result = 0;
        
        if(HEURISTIC1){ //H1: Kies Node eerder, hoe minder kinderen het heeft
            // enkel unassigned in de HashMap please!
            // Sort result op values.size() met kleinste vooraan:

            for(Map.Entry<Integer,ArrayList<Integer>> pair : init.entrySet()){
                if(pair.getValue().size() == 2){
                    result = pair.getKey();
                    break;
                }
            }

        }
        
        if(result==0) return (int) new ArrayList<Integer>(init.keySet()).get(0);
        
        return result;
    }
    
    static ArrayList<Integer> orderValues(ArrayList<Integer> init)
    {
        ArrayList<Integer> result = new ArrayList<Integer>();
        
        result = init;
        
        return result;
    }
    
}

