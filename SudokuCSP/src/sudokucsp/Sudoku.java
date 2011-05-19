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
class Sudoku {
    
    HashMap<Integer, ArrayList<Integer>> assignment;
    
    /**
     * Constructor
     * @see textToAssignment
     * @param input textuele representatie
     */
    Sudoku(String input)
    {
        this.assignment = textToAssignment(input);
    }
    
    /**
     * Copy constructor
     */
    Sudoku(Sudoku s)
    {
        this.assignment = new HashMap<Integer, ArrayList<Integer>>();
        for(int key : s.assignment.keySet())
        {
            ArrayList<Integer> copy = new ArrayList<Integer>(s.assignment.get(key));
            this.assignment.put(key, copy);
        }
    }
    
    private HashMap<Integer, ArrayList<Integer>> textToAssignment(String input)
    {
        // input: .94...13..............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8
        // resultaat: [11:1..9, 12:[9], 13:[4], etc.
        
        input = input.trim();

        HashMap<Integer, ArrayList<Integer>> newassignment = new HashMap<Integer, ArrayList<Integer>>();

        int row = 1;
        int col = 1;
        for(int j=0; j<input.length(); j++)
        {
            char i = input.charAt(j);
            ArrayList<Integer> values = new ArrayList<Integer>();

            int cell = row*10+col;

            if(i == '.')
            {
                Collections.addAll(values, 1,2,3,4,5,6,7,8,9);
            }
            else
            {
                values.add(Integer.parseInt(Character.toString(i)));
            }

            newassignment.put(cell, values);

            col++;
            if(col > 9)
            {
                col = 1;
                row++;
            }
        }

        return newassignment;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for(int i=1; i<=9; i++)
        {
            for(int j=1; j<=9; j++)
            {
                String x;
                ArrayList<Integer> temp = this.assignment.get(i*10+j);
                if(temp.isEmpty())
                {
                    x = "X";
                }
                else if(temp.size() > 1)
                {
                    x = ".";
                }
                else
                {
                    x = temp.get(0).toString();
                }
                sb.append(x);
            }
        }
        return sb.toString();
    }
    
    ArrayList<Integer> getCell(int c)
    {
        return this.assignment.get(c);
    }
    
    void setCell(int c, ArrayList<Integer> vs)
    {
        this.assignment.put(c, vs);
    }    

    /**
     * Kijkt of de sudoku consistent is:
     * (1) elke waarde is uit 1..9
     * (2) elke waarde die assigned is mag maar 1x voorkomen in zijn rij en kolom
     *
     * @return consistent of niet
     */
    boolean isConsistent()
    {
        // (1)
        boolean one = true;
        for(ArrayList<Integer> values : this.assignment.values())
        {
            if(!Arrays.asList(1,2,3,4,5,6,7,8,9).containsAll(values) || values.isEmpty())
            {
                one = false;
            }
        }
        // (2)
        // pak alle cells die assigned zijn
        //def assigned = this.assignment.findAll{ it.value.size() == 1 }
        HashMap<Integer, ArrayList<Integer>> assigned = getAssignedVariables(); // wel gebruik maken van methods he
        // voor elke assigned cell
        boolean two = true;
        for(Map.Entry<Integer, ArrayList<Integer>> it : assigned.entrySet())
        {
            // kijk of ie 1x voorkomt in zijn row en column
            boolean inrow = onlyAppearsOnceInRow(it.getValue().get(0), it.getKey());
            boolean incol = onlyAppearsOnceInColumn(it.getValue().get(0), it.getKey());
            boolean inreg = onlyAppearsOnceIn3by3(it.getValue().get(0), it.getKey());
            if( !( inrow && incol && inreg ) )
            {
                two = false;
            }
        }

        return one && two;
    }
    
    HashMap<Integer, ArrayList<Integer>> getAssignedInGeneral(HashMap<Integer, ArrayList<Integer>> context)
    {
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        for(Map.Entry<Integer, ArrayList<Integer>> pair : context.entrySet())
        {
            if(pair.getValue().size() == 1)
            {
                result.put(pair.getKey(), new ArrayList<Integer>(pair.getValue()));
            }
        }
        return result;
    }
    
    HashMap<Integer, ArrayList<Integer>> getNotAssignedInGeneral(HashMap<Integer, ArrayList<Integer>> context)
    {
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        for(Map.Entry<Integer, ArrayList<Integer>> pair : context.entrySet())
        {
            if(pair.getValue().size() > 1)
            {
                result.put(pair.getKey(), new ArrayList<Integer>(pair.getValue()));
            }
        }
        return result;
    }
    
    /**
     * Geeft een lijst met cells die assigned zijn en hun waarde
     * @param assignment (in)complete assignment
     * @return lijst met lijst per cel, bijv: [ [ 11, [1] ], [ 63, [5] ] ]
     */
    HashMap<Integer, ArrayList<Integer>> getAssignedVariables()
    {
        return getAssignedInGeneral(this.assignment);
    }
    
    /**
     * Geeft de eerste variabele die nog niet assigned is
     * oftewel een lijst (size > 1) met mogelijke waarden heeft
     * @param incomplete assignment
     * @return lijst met 1 element, cellnr
     */
    HashMap<Integer, ArrayList<Integer>> getNotAssignedVariables()
    {
        return getNotAssignedInGeneral(this.assignment);
    }

    /**
     * Geeft een lijst met cells die assigned zijn in de row en hun waarde
     * @param c : cell number
     * @return lijst met lijst per cel, bijv: [ [ 11, [1] ], [ 12, [5] ] ]
     */
    HashMap<Integer, ArrayList<Integer>> getAssignedInRow(int c)
    {
        return getAssignedInGeneral(getRow(c));
    }

    /**
     * Geeft een lijst met cells die niet assigned zijn in de row en hun waarde
     * @param c : cell number
     * @return lijst met lijst per cel, bijv: [ [ 11, [1,2] ], [ 12, [3,5,6] ] ]
     */
    HashMap<Integer, ArrayList<Integer>> getNotAssignedInRow(int c)
    {
        return getNotAssignedInGeneral(getRow(c));
    }

    /**
     * Geeft een lijst met cells die assigned zijn in de col en hun waarde
     * @param c : cell number
     * @return lijst met lijst per cel, bijv: [ [ 11, [1] ], [ 21, [5] ] ]
     */
    HashMap<Integer, ArrayList<Integer>> getAssignedInCol(int c)
    {
        return getAssignedInGeneral(getCol(c));
    }

    /**
     * Geeft een lijst met cells die niet assigned zijn in de col en hun waarde
     * @param c : cell number
     * @return lijst met lijst per cel, bijv: [ [ 11, [1,2] ], [ 21, [1,5] ] ]
     */
    HashMap<Integer, ArrayList<Integer>> getNotAssignedInCol(int c)
    {
        return getNotAssignedInGeneral(getCol(c));
    }
    
    /**
     * Geeft een lijst met cells die assigned zijn in de reg en hun waarde
     * @param c : cell number
     * @return lijst met lijst per cel, bijv: [ [ 11, [1] ], [ 21, [5] ] ]
     */
    HashMap<Integer, ArrayList<Integer>> getAssignedInReg(int c)
    {
        return getAssignedInGeneral(getReg(c));
    }

    /**
     * Geeft een lijst met cells die niet assigned zijn in de reg en hun waarde
     * @param c : cell number
     * @return lijst met lijst per cel, bijv: [ [ 11, [1,2] ], [ 21, [1,5] ] ]
     */
    HashMap<Integer, ArrayList<Integer>> getNotAssignedInReg(int c)
    {
        return getNotAssignedInGeneral(getReg(c));
    }

    /*
     * Returned de row waar de huidige cell in voorkomt
     * @param c cellNr
     * @return srow lijst met cells uit de row
     */
    HashMap<Integer, ArrayList<Integer>> getRow(int c)
    {
        // pak alle cells in de sudoku in dezelfde rij als cell
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        for(Map.Entry<Integer, ArrayList<Integer>> pair : this.assignment.entrySet())
        {
            if(getRowNrFromCellNr(pair.getKey()) == getRowNrFromCellNr(c))
            {
                result.put(pair.getKey(), new ArrayList<Integer>(pair.getValue()));
            }
        }
        return result;
    }

    /*
     * Returned de col waar de huidige cell in voorkomt
     * @param c cellNr
     * @return scol lijst met cells uit de col
     */
    HashMap<Integer, ArrayList<Integer>> getCol(int c)
    {
        // pak alle cells in de sudoku in dezelfde kolom als cell
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        for(Map.Entry<Integer, ArrayList<Integer>> pair : this.assignment.entrySet())
        {
            if(getColNrFromCellNr(pair.getKey()) == getColNrFromCellNr(c))
            {
                result.put(pair.getKey(), new ArrayList<Integer>(pair.getValue()));
            }
        }
        return result;
    }

    /**
     * Returned de 3by3 region waar de huidige cell in voorkomt
     * @param c : cellNr
     * @return sreg : lijst met cells uit de region
     */
    HashMap<Integer, ArrayList<Integer>> getReg(int c){
        int rowNr = getRowNrFromCellNr(c); // Retrieve current row
        int rowReg = rowNr%3; // Calculate relative row number (in a region of 3by3)
        List rows = new ArrayList();
        int colNr = getColNrFromCellNr(c); // Retrieve current column
        int colReg = colNr%3; // Calculate relative col number (in a region of 3by3)
        List cols = new ArrayList();

        //determine other rows
        if(rowReg == 1){
            rows = Arrays.asList(rowNr, rowNr+1, rowNr+2); // Current region in rows
        } else if(rowReg == 2){
            rows = Arrays.asList(rowNr-1, rowNr, rowNr+1); // Current region in rows
        } else if(rowReg == 0){
            rows = Arrays.asList(rowNr-2, rowNr-1, rowNr); // Current region in rows
        }

        //determine other columns
        if(colReg == 1){
            cols = Arrays.asList(colNr, colNr+1, colNr+2); // Current region in columns
        } else if(colReg == 2){
            cols = Arrays.asList(colNr-1, colNr, colNr+1);// Current region in columns
        } else if(colReg == 0){
            cols = Arrays.asList(colNr-2, colNr-1, colNr);// Current region in columns
        }

        // Retrieve all cells that fit in these col/row constraints
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        for(Map.Entry<Integer, ArrayList<Integer>> pair : this.assignment.entrySet())
        {
            boolean un = cols.contains(getColNrFromCellNr(pair.getKey()));
            boolean deux = rows.contains(getRowNrFromCellNr(pair.getKey()));
            if( un && deux )
            {
                result.put(pair.getKey(), new ArrayList<Integer>(pair.getValue()));
            }
            /*
            def sreg = this.assignment.findAll{
                cols.contains(getColFromCellNr(it.key)) &&
                rows.contains(getRowFromCellNr(it.key))
            }
            */
        }
        return result;
    }
    
    /**
     * Kijkt of waarde x eenmaal voorkomt in de row van cell
     * @param v waarde
     * @param c cell
     */
    boolean onlyAppearsOnceInRow(int v, int c)
    {
        // pak alle cells in de sudoku in dezelfde rij als cell
        //def srow = this.assignment.findAll{ getRowNrFromCellNr(it.key) == getRowNrFromCellNr(c) }
        return onlyAppearsOnceInRange(v, getRow(c));
    }

    /**
     * Kijkt of waarde x eenmaal voorkomt in de column van cell
     * @param v waarde
     * @param c cell
     */
    boolean onlyAppearsOnceInColumn(int v, int c)
    {
        // pak alle cells in de sudoku in dezelfde kolom als cell
        //def scol = this.assignment.findAll{ getColNrFromCellNr(it.key) == getColNrFromCellNr(c) }
        return onlyAppearsOnceInRange(v, getCol(c));
    }

    /**
     * Kijkt of waarde eenmaal voorkomt in een 3by3 region van de cell
     * @param v waarde
     * @param c cellnr
     */
    boolean onlyAppearsOnceIn3by3(int v, int c) {
        // pak alle cells in de sudoku in dezelfde region als cell
        return onlyAppearsOnceInRange(v, getReg(c));
    }

    /**
     * Kijkt of waarde x eenmaal of niet voorkomt in een groepje cells
     * v waarde
     * range groepje cells (deel van een Map)
     */
    boolean onlyAppearsOnceInRange(int v, HashMap<Integer, ArrayList<Integer>> range)
    {
        int count = 0;
        for(ArrayList<Integer> vs : range.values())
        {
            for(int i : vs)
            {
                if(i == v && vs.size() == 1) { count++; }
            }
        }
        return count == 1;
    }
    
    /**
     * Kijkt of waarde x voorkomt in een groepje cells
     * v waarde
     * range groepje cells (deel van een Map)
     */
    boolean appearsInRange(int v, HashMap<Integer, ArrayList<Integer>> range)
    {
        
        int count = 0;
        for(ArrayList<Integer> vs : range.values())
        {
            for(int i : vs)
            {
                if(i == v && vs.size() == 1) { count++; }
            }
        }
        return count >= 1;
    }

    /**
     * Kijkt of waarde v in c inconsistentie zou geven
     * @param v     : value voor de cell
     * @param c     : cell number
     * @return  boolean : true -> consistent ; false -> inconsistent
     */
    boolean cellConsistent(int v, int c){
        // pak alle cells in de sudoku in dezelfde kolom als cell
        HashMap<Integer, ArrayList<Integer>> scol = getCol(c);
        scol.remove(c);
        // pak alle cells in de sudoku in dezelfde rij als cell
        HashMap<Integer, ArrayList<Integer>> srow = getRow(c);
        srow.remove(c);
        // pak alle cells in de sudoku in dezelfde region als cell
        HashMap<Integer, ArrayList<Integer>> sreg = getReg(c);
        sreg.remove(c);
        return !appearsInRange(v,scol) && !appearsInRange(v,srow) && !appearsInRange(v,sreg);
    }

    /**
     * Haalt het rownr uit een cellnr
     * c cellnr
     */
    int getRowNrFromCellNr(int c)
    {
        return (int) Math.floor(c / 10);
    }

    /**
     * Haalt het colnr uit een cellnr
     * c cellnr
     */
    int getColNrFromCellNr(int c)
    {
         return c % 10;
    }

    /**
     * Returned het aantal constraints (influencing givens) op de meegegeven cell
     * c cellnr
     */
    int getNrConstraints(int c){

        /*Tjek vanuit deze cell
        HashMap<Integer, ArrayList<Integer>> row = getAssignedInRow(c);
        HashMap<Integer, ArrayList<Integer>> col = getAssignedInCol(c);
        HashMap<Integer, ArrayList<Integer>> reg = getAssignedInReg(c);
        
        return 0 + row.size() + col.size() + reg.size();*/

        //alternatief: tjek vanuit assigned values
        int result = 0;
        HashMap<Integer, ArrayList<Integer>> ass = getAssignedVariables();
        for(Map.Entry<Integer,ArrayList<Integer>> pair : ass.entrySet()){
            int cell = pair.getKey();
            // mis nog wel wat van de region nu
            if(getColNrFromCellNr(cell)==getColNrFromCellNr(c)||getRowNrFromCellNr(cell)==getRowNrFromCellNr(c)){
                result ++;
            }
        }
        return result;
    }

    /***********************************************************************
     ***********************REVISE & SUDOKU TECHNIQUES**********************
     ***********************************************************************/
    /**
     * Revise:
     * Zorgt dat binary constraints voldaan zijn, i.e. delete impossible values
    */
    boolean revise(){
        boolean delete = false; //Did we delete something?
        if(Solver.REVISE || Solver.HIDDENSINGLES)
        {
            for(Map.Entry<Integer, ArrayList<Integer>> pair : this.assignment.entrySet())
            {
                int c = pair.getKey(); //cellNr
                ArrayList<Integer> values = pair.getValue(); //possible values
                
                if(Solver.REVISE)
                {
                    ArrayList<Integer> toRemove = new ArrayList<Integer>();
                    // NORMAL REVISE:
                    for(int v : values) //for each possible value of cell
                    {
                        if(!cellConsistent(v,c)) //if value is not consistent with sudoku
                        {
                            toRemove.add(v); //remove value
                            delete = true;
                        }
                    }
                    for(int i : toRemove)
                    {
                        values.remove(values.indexOf(i));
                    }
                }

                //update mogelijke values in cell
                //setCell(c,values); gebeurt automatisch al door Map.Entry

                /*
                 * TODO zorgen dat hiddenSingle niet overbodig tjekt
                 * Ik had wat printjes erin bij diffRow gestopt, dit was de eerste 2 outputs:
                 *
    values: [2, 5, 6, 7, 8]
    otherValues: [1, 2, 3, 4, 5, 6, 7, 8, 9]
    diff_before: [2, 5, 6, 7, 8]
    diff_after: []
    values: [9] <---- dit is nutteloze check, dit vakje heeft dus nu al waarde Assigned!
    otherValues: [1, 2, 3, 4, 5, 6, 7, 8, 9]
    diff_before: [9]
    diff_after: []
                 * en dit was echt niet de enige. die komen dus al totaal revised uit loop van net.
                 *
                 */
                //HIDDEN SINGLE:
                if(values.size() > 1) //KABAM! bijna de helft van de tijd eraf gesneden!
                {
                    if(Solver.HIDDENSINGLES)
                    { 
                        if(hiddenSingle(c,values)){
                            delete = true; //revise did something
                        }
                    }
                    if(Solver.NAKEDPAIRS)
                    {
                        if(nakedPairs(c,values)){
                            delete = true;
                        }
                    }
                    if(Solver.HIDDENPAIRS)
                    {
                        if(hiddenPairs(c,values)){
                            delete = true;
                        }
                    }
                }
            }
        }
        //returned of revise iets gedaan heeft, is handig in een while.
        return delete;
    }

    /*
     * Open Singles
     * technique: http://www.learn-sudoku.com/open-singles.html
     * Fill in the last remaining number.
     * Probably very redundant and without actual performance boosting:
     *  seems to be inherent to revise already.
     * @result this.assignment might be adjusted.
    
    def openSingles() {
       for(i in 1..9){
           // Check every row for open singles

           def inRow = getAssignedInRow(i) //all assigned values & their keys
           def keyR = []
           def valueR = []
           if(inRow.size()==8){ //if only one value is missing
 // e.g. [ [11,1], [12,2], [13,3], [14,4], [15,5], [16,6], [17,7], [19,8]]
               inRow.each{ //for every given value in the list
                   keyR << getColNrFromCellNr(it[0]) //retrieve the col in row
                   valueR << it[1] //retrieve the value
               }
               def missingValueR = full - valueR //one value will remain
               def missingKeyR = full - keyR// one column will remain
               missingKeyR = missingKeyR[0]+10*i //assemble cell key
               this.assignment[missingKeyR] = missingValueR //assign the value
           }

           //            Check every column for open singles
           
           def inCol = getAssignedInCol(i) //all assigned values & their keys
           def keyC = []
           def valueC = []
           if(inCol.size()==8){ //if only one value is missing
 // e.g. [ [11,1], [21,2], [31,3], [41,4], [51,5], [61,6], [71,7], [91,8]]
               inCol.each{ //for every given value in the list
                   keyC << getRowNrFromCellNr(it[0]) //retrieve the row in col
                   valueC << it[1] //retrieve the value
               }
               def missingValueC = full - valueC //one value will remain
               def missingKeyC = full - keyC// retrieve empty col in row
               missingKeyC = missingKeyC[0]+10*i //assemble cell key
               this.assignment[missingKeyC] = missingValueC //assign the value
           }
       }

   }
   */

    /*
     *Hidden Singles or Pinned Squares
     *Hidden: http://www.learn-sudoku.com/hidden-singles.html
     *Pinned: http://www.brainbashers.com/sudokupinnedsquares.asp
     *If list of values in the cell contains a unique value for its row,col
     *then that is a Hidden Single and we can assign that value to the cell.
     *@param cellNr : nr of the cell
     *@return values : (possibly) new values for the cell
    */

    boolean hiddenSingle(int cellNr, ArrayList<Integer> values) {
        boolean delete = false; // bijhouden of we iets deleten

        //ALL: Intialize; Retrieve other cells
        
        //retrieve oningevulde rowcells,haal huidige er vanaf
        HashMap<Integer, ArrayList<Integer>> row = getNotAssignedInRow(cellNr); 
        row.remove(cellNr);
        
        HashMap<Integer, ArrayList<Integer>> col = getNotAssignedInCol(cellNr); 
        col.remove(cellNr);
        
        HashMap<Integer, ArrayList<Integer>> reg = getNotAssignedInReg(cellNr); 
        reg.remove(cellNr);

        //ALL: combine values from the other cells, compare with current values
        //ROW: Now put all the values from the other cells in one list
        HashSet<Integer> otherValuesRow = new HashSet<Integer>();
        for(ArrayList<Integer> valuelist : row.values())
        {
            otherValuesRow.addAll(valuelist);
        }
        //check what values are in current cell, but not in the others
        ArrayList<Integer> difRow = new ArrayList<Integer>(values);
        //System.out.println("values: "+values);
        //System.out.println("otherValues: "+otherValuesRow);
        //System.out.println("diff_before: "+difRow);
        difRow.removeAll(otherValuesRow);
        //System.out.println("diff_after: "+difRow);

        //COL: Now put all the values from the other cells in one list
        HashSet<Integer> otherValuesCol = new HashSet<Integer>();
        for(ArrayList<Integer> valuelist : col.values())
        {
            otherValuesCol.addAll(valuelist);
        }
        //check what values are in current cell, but not in the others
        ArrayList<Integer> difCol = new ArrayList<Integer>(values);
        difCol.removeAll(otherValuesCol);

        //REG: Now put all the values from the other cells in one list
        HashSet<Integer> otherValuesReg = new HashSet<Integer>();
        for(ArrayList<Integer> valuelist : reg.values())
        {
            otherValuesReg.addAll(valuelist);
        }
        //check what values are in current cell, but not in the others
        ArrayList<Integer> difReg = new ArrayList<Integer>(values);
        difReg.removeAll(otherValuesReg);

        //ALL: now check if they are singles (and changed)
        if(difRow.size() == 1 && !difRow.equals(values)){ //whoehoe! hidden single
            setCell(cellNr,difRow);
            delete = true;
        }
        else if(difCol.size() == 1 && !difCol.equals(values)){ //whoehoe! hidden single
            setCell(cellNr,difCol);
            delete = true;
        }
        else if(difReg.size() == 1 && !difReg.equals(values)){ //whoehoe! hidden single
            setCell(cellNr,difReg);
            delete = true;
        }
        else if(difRow.size() > 1 || difCol.size() > 1 || difReg.size() > 1){
            //sudoku is wrong :O
        }
        
        return delete;
    }
    
    /**
     * Poging tot Naked Pairs
     * http://www.learn-sudoku.com/naked-pairs.html
     * @param cellNr
     * @param values
     * @return of iets verwijderd is
     */
    boolean nakedPairs(int cellNr, ArrayList<Integer> values)
    {
        boolean delete = false;
        
        // alleen pairs
        if(values.size() != 2) return false;
        // triplets maakt langzamer:
        // if(values.size() != 2 || values.size() != 3) return false;
        
        // pak row van deze cell
        HashMap<Integer, ArrayList<Integer>> row = getNotAssignedInRow(cellNr); 
        // tief cell er uit
        row.remove(cellNr);
        // zoek pairs met deze cell
        boolean rowresult = nakedPairsGeneral(values, row);
        
        HashMap<Integer, ArrayList<Integer>> col = getNotAssignedInCol(cellNr); 
        col.remove(cellNr);
        boolean colresult = nakedPairsGeneral(values, col);
        
        HashMap<Integer, ArrayList<Integer>> reg = getNotAssignedInReg(cellNr); 
        reg.remove(cellNr);
        boolean regresult = nakedPairsGeneral(values, reg);
        
        // als iets verwijderd is
        delete = rowresult || colresult || regresult;
        return delete;
    }
    
    /**
     * Generieke Naked Pairs methode
     * @param values
     * @param part
     * @return of iets verwijderd is
     */
    boolean nakedPairsGeneral(ArrayList<Integer> values, HashMap<Integer, ArrayList<Integer>> part)
    {
        boolean delete = false;
        
        // lijst met cellnrs die pairen met de huidige cell
        ArrayList<Integer> matches = new ArrayList<Integer>();
        
        // zoek en bewaar alle cells in part met exact dezelfde values
        for(Map.Entry<Integer, ArrayList<Integer>> pair : part.entrySet())
        {
            if(pair.getValue().containsAll(values) && values.containsAll(pair.getValue()))
            {
                matches.add(pair.getKey());
            }
        }
        
        // nakedPairs() geven we alleen value-lijsten door met waarde 2(/3/4)
        // maar aangezien we de 'huidige' cell uit part hebben gehaald
        // klopt deze conditie altijd als we een pair(/triplet/quad) hebben gevonden
        if(matches.size() == values.size() - 1)
        {
            for(Map.Entry<Integer, ArrayList<Integer>> pair : part.entrySet())
            {
                if(!matches.contains(pair.getKey())) // niet de gepairde aanpassen
                {
                    ArrayList<Integer> newvalues = new ArrayList<Integer>(pair.getValue());
                    boolean changed = newvalues.removeAll(values);
                    if(changed) delete = true;
                    setCell(pair.getKey(), newvalues);
                }
            }
        }
        
        return delete;
    }
    
    boolean hiddenPairs(int cellNr, ArrayList<Integer> values)
    {
        boolean delete = false;
        
        // alleen pairs
        if(values.size() != 2) return false;
        // triplets maakt langzamer:
        // if(values.size() != 2 || values.size() != 3) return false;
        
        // pak row van deze cell
        HashMap<Integer, ArrayList<Integer>> row = getNotAssignedInRow(cellNr); 
        // zoek pairs met deze cell
        boolean rowresult = hiddenPairsGeneral(values, row);
        
        HashMap<Integer, ArrayList<Integer>> col = getNotAssignedInCol(cellNr); 
        boolean colresult = hiddenPairsGeneral(values, col);
        
        HashMap<Integer, ArrayList<Integer>> reg = getNotAssignedInReg(cellNr);
        boolean regresult = hiddenPairsGeneral(values, reg);
        
        // als iets verwijderd is
        delete = rowresult || colresult || regresult;
        return delete;
    }
    
    // niet cell weghalen uit part!
    boolean hiddenPairsGeneral(ArrayList<Integer> values, HashMap<Integer, ArrayList<Integer>> part)
    {
        boolean delete = false;
        
        // map met per value de cells waar de value in voor komt
        HashMap<Integer, ArrayList<Integer>> inverse = new HashMap<Integer, ArrayList<Integer>>();
        for(int i : new int[] {1,2,3,4,5,6,7,8,9})
        {
            inverse.put(i, new ArrayList<Integer>());
        }
        
        for(Map.Entry<Integer, ArrayList<Integer>> pair : part.entrySet())
        {
            for(int i : pair.getValue())
            {
                inverse.get(i).add(pair.getKey());
            }
        }
        
        for(Map.Entry<Integer, ArrayList<Integer>> pair : inverse.entrySet())
        {
            ArrayList<Integer> p1 = pair.getValue(); // 1 => 11,12
            
            for(Map.Entry<Integer, ArrayList<Integer>> otherpair : inverse.entrySet())
            {
                ArrayList<Integer> p2 = otherpair.getValue(); // 2 => 11,12
                
                if(p1.containsAll(p2) && p2.containsAll(p1))
                {
                    // hidden pair gevonden
                    ArrayList<Integer> hcells = new ArrayList<Integer>(pair.getValue()); // 11,12
                    ArrayList<Integer> hvalues = new ArrayList<Integer>();
                    
                    for(Map.Entry<Integer, ArrayList<Integer>> ppair : part.entrySet())
                    {
                        Integer pcell = ppair.getKey();
                        ArrayList<Integer> pvalues = ppair.getValue();
                        
                        if(!hcells.contains(pcell)) // niet deel van hidden pair
                        {
                            boolean changed = pvalues.removeAll(hvalues);
                            if(changed) delete = true;
                        }
                    }
                }
            }
        }
        
        return delete;
    }
	
}

