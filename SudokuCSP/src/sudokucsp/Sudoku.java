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
    static final int[] ONETONINE = {1, 2, 3, 4, 5, 6, 7, 8, 9}; // used in multiple methods as check.
    
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
        for(int key : s.assignment.keySet())
        {
            ArrayList<Integer> copy = (ArrayList<Integer>) s.assignment.get(key).clone();
            this.assignment.put(key, copy);
        }
    }
    
    private HashMap<Integer, ArrayList<Integer>> textToAssignment(String input)
    {
        // input: .94...13..............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8
        // resultaat: [11:1..9, 12:[9], 13:[4], etc.
        
        input = input.trim();

        HashMap newassignment = new HashMap<Integer, ArrayList<Integer>>();

        int row = 1;
        int col = 1;
        for(int j=0; j<input.length(); j++)
        {
            String i = new StringBuilder(input.charAt(j)).toString();
            ArrayList values = new ArrayList<Integer>();

            int cell = row*10+col;

            if(i.equals("."))
            {
                List temp = Arrays.asList(ONETONINE);
                Collections.addAll(values, temp);
            }
            else
            {
                values.add(Integer.parseInt(i));
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
            for(int j=1; j<=9; i++)
            {
                String x;
                if(this.assignment.get(i*10+j).size() > 1)
                {
                    x = ".";
                }
                else
                {
                    x = this.assignment.get(i*10+j).get(0).toString();
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
            if(!Arrays.asList(ONETONINE).containsAll(values) && values.size() > 0)
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
            if( !( onlyAppearsOnceInRow(it.getValue().get(0), it.getKey()) && onlyAppearsOnceInColumn(it.getValue().get(0), it.getKey()) && onlyAppearsOnceIn3by3(it.getValue().get(0), it.getKey()) ) )
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
                result.put(pair.getKey(), (ArrayList<Integer>) pair.getValue().clone());
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
                result.put(pair.getKey(), (ArrayList<Integer>) pair.getValue().clone());
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
                result.put(pair.getKey(), (ArrayList<Integer>) pair.getValue().clone());
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
                result.put(pair.getKey(), (ArrayList<Integer>) pair.getValue().clone());
            }
        }
        return result;
    }

    /**
     *TODO Zorgen dat de 3x3 region niet telkens overnieuw hoeft worden berekend
     * Returned de 3by3 region waar de huidige cell in voorkomt
     * @param c : cellNr
     * @return sreg : lijst met cells uit de region
     */
    HashMap<Integer, ArrayList<Integer>> getReg(int c){
        int rowNr = getRowNrFromCellNr(c); // Retrieve current row
        int rowReg = rowNr%3; // Calculate relative row number (in a region of 3by3)
        int[] rows = new int[3];
        int colNr = getColNrFromCellNr(c); // Retrieve current column
        int colReg = colNr%3; // Calculate relative col number (in a region of 3by3)
        int[] cols = new int[3];

        //determine other rows
        if(rowReg == 1){
            rows = new int[] {rowNr, rowNr+1, rowNr+2}; // Current region in rows
        } else if(rowReg == 2){
            rows = new int[] {rowNr-1, rowNr, rowNr+1}; // Current region in rows
        } else if(rowReg == 0){
            rows = new int[] {rowNr-2, rowNr-1, rowNr}; // Current region in rows
        }

        //determine other columns
        if(colReg == 1){
            cols = new int[] {colNr, colNr+1, colNr+2}; // Current region in columns
        } else if(colReg == 2){
            cols = new int[] {colNr-1, colNr, colNr+1};// Current region in columns
        } else if(colReg == 0){
            cols = new int[] {colNr-2, colNr-1, colNr};// Current region in columns
        }

        // Retrieve all cells that fit in these col/row constraints
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        for(Map.Entry<Integer, ArrayList<Integer>> pair : this.assignment.entrySet())
        {
            if(Arrays.asList(cols).contains(getColNrFromCellNr(pair.getKey())) && Arrays.asList(rows).contains(getColNrFromCellNr(c)))
            {
                result.put(pair.getKey(), (ArrayList<Integer>) pair.getValue().clone());
            }
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
                if(i == v) { count++; }
            }
        }
        return count <= 1;
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
                if(i == v) { count++; }
            }
        }
        return count <= 1;
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
     *TODO zorgen dat hidden single cell 11 '7' maakt.
     *
     * Revise:
     * Zorgt dat binary constraints voldaan zijn, i.e. delete impossible values
    */
    boolean revise(){
        boolean delete = false; //Did we delete something?
        for(Map.Entry<Integer, ArrayList<Integer>> pair : this.assignment.entrySet())
        {
            int c = pair.getKey(); //cellNr
            ArrayList<Integer> values = pair.getValue(); //possible values
            ArrayList<Integer> toRemove = new ArrayList<Integer>();
            // NORMAL REVISE:
            for(int v : values) //for each possible value of cell
            {
                if(!cellConsistent(v,c)) //if value is not consistent with sudoku
                {
                    toRemove.add(v); //remove value
                    delete = true;
                    if(values.isEmpty())
                    {
                        break;
                    }
                }
            }
            // TODO checken of dit correct is omgeschreven
            for(int i : toRemove)
            {
                values.remove(values.indexOf(i));
            }
            

            //update mogelijke values in cell
            setCell(c,values);

            /*
             *TODO hidden single werkend krijgen
             */
            //HIDDEN SINGLE:
            if(hiddenSingle(c,values)){
                delete = true; //revise did something
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
        //check what values are in this cell, but not in the others
        ArrayList<Integer> difRow = values;
        difRow.removeAll(otherValuesRow);

        //COL: Now put all the values from the other cells in one list
        HashSet<Integer> otherValuesCol = new HashSet<Integer>();
        for(ArrayList<Integer> valuelist : col.values())
        {
            otherValuesCol.addAll(valuelist);
        }
        //check what values are in this cell, but not in the others
        ArrayList<Integer> difCol = values;
        difCol.removeAll(otherValuesCol);

        //REG: Now put all the values from the other cells in one list
        HashSet<Integer> otherValuesReg = new HashSet<Integer>();
        for(ArrayList<Integer> valuelist : reg.values())
        {
            otherValuesReg.addAll(valuelist);
        }
        //check what values are in this cell, but not in the others
        ArrayList<Integer> difReg = values;
        difReg.removeAll(otherValuesReg);

        //ALL: now check if they are singles (and changed)
        if(difRow.size() == 1 && !difRow.equals(values)){ //whoehoe! hidden single
            setCell(cellNr,difRow);
            /*println cellNr
            println values
            println difRow
            println 'unique in row'*/
            delete = true;
        }
        else if(difCol.size() == 1 && !difCol.equals(values)){ //whoehoe! hidden single
            setCell(cellNr,difCol);
            /*println cellNr
            println values
            println difCol
            println 'unique in col'*/
            delete = true;
        }
        else if(difReg.size() == 1 && !difReg.equals(values)){ //whoehoe! hidden single
            setCell(cellNr,difReg);
            /*println cellNr
            println values
            println difReg
            println 'unique in reg'*/
            delete = true;
        }
        else if(difRow.size() > 1 || difCol.size() > 1 || difReg.size() > 1){
            /*println 'faulty sudoku values'
            println cellNr
            println values
            println nrow
            println row
            println otherValuesRow
            println difRow
            println ncol
            println col
            println otherValuesCol
            println difCol
            println nreg
            println reg
            println otherValuesReg
            println difReg*/

            //sudoku is wrong, print debug stuff
        }
        
        return delete;
    }
	
}

