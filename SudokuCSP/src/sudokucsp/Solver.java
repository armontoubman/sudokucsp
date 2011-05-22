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
    
    static boolean PRINT = false;

    //SET VALUE REMOVAL TECHNIQUES
    static boolean REVISE = true;
    static boolean HIDDENSINGLES = true;
    static boolean NAKEDPAIRS = true;
    static boolean HIDDENPAIRS = true; // buggy

    //SET ORDERING
    static boolean ORDERVARIABLES = true; // if false, no heuristics
    static boolean ORDERVALUES = false;

    //COUNT EFFECTIVENESS
    static int revise;
    static int hSingle;
    static int nPair;
    static int hPair;

    //SET HEURISTICS for ordering
    static boolean HEURISTIC1 = false; // Nr of Children
    static boolean HEURISTIC13 = true; // Heuristic 1&3
    //static boolean HEURISTIC2 = false;
    static boolean HEURISTIC3 = false; // Nr of Constraints
    //static boolean HEURISTIC4 = false;
    
    static Sudoku solve(Sudoku s)
    {
        //COUNT EFFECTIVENESS
        /*revise = 0;
        hSingle = 0;
        nPair = 0;
        hPair = 0;*/
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
/*
        boolean removing = true; //Initiate loop
        while(removing){ // While revise has removed values
            removing = s.revise(); // revise (again)
        }
*/
        // pak een variable die nog niet assigned is
        HashMap<Integer, ArrayList<Integer>> variables = s.getNotAssignedVariables();
        // Instead of while-ing our revise, we apply smart-revising: revise2.
        //TODO zorgen dat revise2 logischerwijs wel sneller is dan revise
        ArrayList<Integer> keyset = new ArrayList<Integer>(variables.keySet());
        ArrayList<Integer> returnset = new ArrayList<Integer>();
        returnset.addAll(keyset);
        while(!returnset.isEmpty()){ //Until no cells are revised anymore
            //System.out.println("Previous Input: "+returnset);
            returnset = s.revise2(keyset); //Revise the current crop
            //System.out.println("Revised: "+returnset);
            returnset = s.affected(returnset); //Add revised cell's row/col/reg
            //System.out.println("Next Input: "+returnset);
        }

        // pak een variable die nog niet assigned is
        variables = s.getNotAssignedVariables();
        // alles assigned? hopelijk klaar
        if(variables.isEmpty()){
            if(s.isConsistent()){ // klaar!
                return s;
            }
            else { // jammer, vol maar fout!
                return null;
            }
        }

        

                            // TIMING //
        // revise on sudoku_training.txt
            //31 & 33 seconds
        // revise2 on sudoku_training.txt
            //37 & 36 seconds with old affected (revise affected ones)
            //34 & 35 seconds with updated affected (revise affected ones)
            //34 & 31 seconds without affected, with returnset (revise a subset)
            //31 & 32 seconds without affected, with keyset ( == revise)
        // revise on top95.txt
            //12 & 11 seconds
        // revise2 on top95.txt
            //14 & 14 seconds with old affected (revise affected ones)
            //14 & 13 seconds with updated affected (revise affected ones)
            //13 & 14 seconds without affected, with returnset (revise a subset)
            //11 & 12 seconds without affected, with keyset ( == revise)
        // ERGO: implementation of retrieving affected cells is slower than just repeating it for all
        
    
        //COUNT EFFECTIVENESS
        revise += s.count_revise;
        hSingle += s.count_hSingle;
        nPair += s.count_nPair;
        hPair += s.count_hPair;

        int c;
        if(ORDERVARIABLES)
        {
            c = orderVariables(variables, s);
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

                    if(PRINT)
                    {
                        for(int i=0; i<depth; i++) { System.out.print(" "); }
                        System.out.println(testcopy);
                        //println s.assignment //ff wat handiger bij t debuggen/improven
                    }
                
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
    static int orderVariables(HashMap<Integer, ArrayList<Integer>> init, Sudoku s){
        int result = 0;
        
        int lowestcell=-1, lowestvalues=-1, cell=-1, values=-1, h3 = -1, highesth3 = -1;

        for(Map.Entry<Integer,ArrayList<Integer>> pair : init.entrySet()){
                cell = pair.getKey();
                if(HEURISTIC1 || HEURISTIC13){ //H1: Kies Node eerder, hoe minder kinderen het heeft
                    values = pair.getValue().size();

                    if(lowestcell == -1 || values < lowestvalues)
                    {
                        if(HEURISTIC13){ // combined Heuristic 3 & 1
                            h3 = s.getNrConstraints(pair.getKey());
                            if(lowestcell == -1 || h3 > highesth3){
                                lowestcell = cell;
                                highesth3 = h3;
                                lowestvalues = values;
                            }
                        } else {
                            lowestcell = cell;
                            lowestvalues = values;
                        }
                    }
                }
                else if(HEURISTIC3){ //H3: Kies Node eerder, hoe meer constrained deze is (som values in row/col/reg)
                    h3 = s.getNrConstraints(pair.getKey());
                    if(lowestcell == -1 || h3 > highesth3){
                        lowestcell = cell;
                        highesth3 = h3;
                    }
                }
        }

        if(lowestcell != -1) result = lowestcell;
        
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

