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
    /* TODO optimizen! Hij doet nu al 70 minuten oid en nog geen sudoku :P
     * TODO add constraint propogation techniques etc. uit slides en hiero:
     * http://kti.mff.cuni.cz/~bartak/constraints/consistent.html
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
    * Constraint propogation: Forward Checking
    * @param
    * @return
    *   PROCEDURE prop(j: INTEGER, VAR D: domains;
    *                   VAR failure: BOOLEAN);
    *   VAR k: INTEGER;
    *   BEGIN
    *       failure := FALSE;
    *       k := j+1;
    *       WHILE k <> n+1 AND NOT failure D0
    *           revise(j,k,D);
    *           failure := (D[k] = {});
    *           k := k+1
    *       END
    *   END prop;
    *
    *   PROCEDURE revise(j,k: INTEGER, VAR D: domain);
    *   BEGIN
    *       D[k] := {d uit D[k] | {(x1,inst[1],...,(xj,inst[j]),(xk,d)}
    *                               is a consistent instantiation}
    *   END revise;
    *
    */
    /* TODO Deze afmaken
    */
   def prop(j,D){ // D = assignment
       failure = false;
       k = j+1;
       while(k != n+1 && !failure){
           revise(j,k,D); //
           failure = (D[k] == null); // no more possible values, stop!
           k = k+1;
       }
       return failure
   }


    /* Arcconsistancy / Binary Constraint
     *
     *@param vi = index of 1st variable
     *@param vj = index of 2nd variable
     *@param assignment = current sudoku
     *@return delete = boolean if something is deleted
     *
     *procedure REVISE(Vi,Vj)
          DELETE <- false;
          for each X in Di do
            if there is no such Y in Dj such that (X,Y) is consistent,
            then
               delete X from Di;
               DELETE <- true;
            endif;
          endfor;
          return DELETE;
        end REVISE

     */
    def revise(vi,vj,assignment){
        delete = false; // initialize : nothing is deleted yet
        a = assignment[vi].clone(); // clone, to keep the indexing correct
        for(x in a){ // every value x from variable i
            cons = false; // guilty until proven innocent
            for(y in assignment[vj]){ // every value y from variable j
                if(consistent(x,vi,y,vj)){ //restricten 2 waarden elkaar niet?
                    cons = true; //dan is er iig 1 value y voor x, dus x is veilig
                }
            }
            if(!cons){ //als er dus helemaal geen y value was die consistent was
                assignment[vi].remove(assignment[vi].indexOf(x)); //delete x
                delete = true; //we hebben wat gedelete
            }
         
        }
        return delete;
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
     *Kijkt of twee gegeven waardes uit de Sudoku consistent zijn:
     *elke waarde mag maar 1x voorkomen in zijn rij en kolom
     *@param x : value van variabele 1
     *@param vx : cell/key van variabele 1
     *@param y : value van variabele 2
     *@param vy : cell/key van variabele 2
     *@return boolean : of de column & row constraints kloppen
     */
    def consistent(x,vx,y,vy){
        //innocent until proven guilty
        col = true;
        row = true;
        //always consistent if not the same value...
        if(x == y){
            //Decypher rows & columns
            rowX = getRowFromCellNr(vx);
            colX = getColFromCellNr(vx);
            rowY = getRowFromCellNr(vy);
            colY = getColFromCellNr(vy);
            //Check for inconsistencies
            if(colX == colY){ //same column
                col = false; //niet coLsistent ;)
            }
            if(rowX == rowY){ //same row
                row = false; //niet consistent
            }
        }
        //Consistent if all constraints are ok
        return col && row
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

