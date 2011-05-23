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
    
    static final int ROWS[] = {0, 0,0,0,0,0,0,0,0,0,0, 1,1,1,1,1,1,1,1,1,0, 2,2,2,2,2,2,2,2,2,0, 3,3,3,3,3,3,3,3,3,0, 4,4,4,4,4,4,4,4,4,0, 5,5,5,5,5,5,5,5,5,0, 6,6,6,6,6,6,6,6,6,0, 7,7,7,7,7,7,7,7,7,0, 8,8,8,8,8,8,8,8,8,0, 9,9,9,9,9,9,9,9,9,0 };
    static final int COLS[] = {0, 0,0,0,0,0,0,0,0,0,0, 1,2,3,4,5,6,7,8,9,0, 1,2,3,4,5,6,7,8,9,0, 1,2,3,4,5,6,7,8,9,0, 1,2,3,4,5,6,7,8,9,0, 1,2,3,4,5,6,7,8,9,0, 1,2,3,4,5,6,7,8,9,0, 1,2,3,4,5,6,7,8,9,0, 1,2,3,4,5,6,7,8,9,0, 1,2,3,4,5,6,7,8,9,0 };
    static final int REGS[] = {0, 0,0,0,0,0,0,0,0,0,0, 1,1,1,2,2,2,3,3,3,0, 1,1,1,2,2,2,3,3,3,0, 1,1,1,2,2,2,3,3,3,0, 4,4,4,5,5,5,6,6,6,0, 4,4,4,5,5,5,6,6,6,0, 4,4,4,5,5,5,6,6,6,0, 7,7,7,8,8,8,9,9,9,0, 7,7,7,8,8,8,9,9,9,0, 7,7,7,8,8,8,9,9,9,0 };
    
    static final int ROWS2[][] = { {}, {11,12,13,14,15,16,17,18,19}, {21,22,23,24,25,26,27,28,29}, {31,32,33,34,35,36,37,38,39}, {41,42,43,44,45,46,47,48,49}, {51,52,53,54,55,56,57,58,59}, {61,62,63,64,65,66,67,68,69}, {71,72,73,74,75,76,77,78,79}, {81,82,83,84,85,86,87,88,89}, {91,92,93,94,95,96,97,98,99} };
    static final int COLS2[][] = { {}, {11,21,31,41,51,61,71,81,91}, {12,22,32,42,52,62,72,82,92}, {13,23,33,43,53,63,73,83,93}, {14,24,34,44,54,64,74,84,94}, {15,25,35,45,55,65,75,85,95}, {16,26,36,46,56,66,76,86,96}, {17,27,37,47,57,67,77,87,97}, {18,28,38,48,58,68,78,88,98}, {19,29,39,49,59,69,79,89,99} };
    static final int REGS2[][] = { {}, {11,12,13,21,22,23,31,32,33}, {14,15,16,24,25,26,34,35,36}, {17,18,19,27,28,29,37,38,39}, {41,42,43,51,52,53,61,62,63}, {44,45,46,54,55,56,64,65,66}, {47,48,49,57,58,59,67,68,69}, {71,72,73,81,82,83,91,92,93}, {74,75,76,84,85,86,94,95,96}, {77,78,79,87,88,89,97,98,99} };

    //COUNT EFFECTIVENESS
    int count_revise = 0;
    int count_hSingle = 0;
    int count_nPair = 0;
    int count_hPair = 0;
    
    Sudoku()
    {
        
    }
    
    void init(String input)
    {
        this.assignment = textToAssignment(input);
    }
    
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
        /* OUDE METHODE
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
        */
        /* NIEUWE METHODE
        int target = getRowNrFromCellNr(c);
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        for(Map.Entry<Integer, ArrayList<Integer>> pair : this.assignment.entrySet())
        {
            if(getRowNrFromCellNr(pair.getKey()) == target)
            {
                result.put(pair.getKey(), new ArrayList<Integer>(pair.getValue()));
            }
        }
        return result;
        */
        /* NOG NIEUWERE METHODE
        int target = getRowNrFromCellNr(c);
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        for(int i=11; i<100; i++)
        {
            if(Sudoku.ROWS[i] == target)
            {
                result.put(i, new ArrayList<Integer>(this.assignment.get(i)));
                if(result.size() == 9) break;
            }
        }
        return result;
        */
        int target = getRowNrFromCellNr(c);
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        int[] cells = ROWS2[target];
        for(int i : cells)
        {
            result.put(i, new ArrayList<Integer>(this.assignment.get(i)));
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
        /* OUDE METHODE
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
        */
        /* NIEUWE METHODE
        int target = getColNrFromCellNr(c);
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        for(Map.Entry<Integer, ArrayList<Integer>> pair : this.assignment.entrySet())
        {
            if(getColNrFromCellNr(pair.getKey()) == target)
            {
                result.put(pair.getKey(), new ArrayList<Integer>(pair.getValue()));
            }
        }
        return result;
        */
        /* NOG NIEUWERE METHODE 
        int target = getColNrFromCellNr(c);
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        for(int i=11; i<100; i++)
        {
            if(Sudoku.COLS[i] == target)
            {
                result.put(i, new ArrayList<Integer>(this.assignment.get(i)));
                if(result.size() == 9) break;
            }
        }
        return result;
        */
        int target = getColNrFromCellNr(c);
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        int[] cells = COLS2[target];
        for(int i : cells)
        {
            result.put(i, new ArrayList<Integer>(this.assignment.get(i)));
        }
        return result;
    }

    /**
     * Returned de 3by3 region waar de huidige cell in voorkomt
     * @param c : cellNr
     * @return sreg : lijst met cells uit de region
     */
    HashMap<Integer, ArrayList<Integer>> getReg(int c){
        
        /* OUDE METHODE
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
        }
        return result;
        */
        
        /* NIEUWE METHODE 
        int target = getRegNrFromCellNr(c);
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        for(Map.Entry<Integer, ArrayList<Integer>> pair : this.assignment.entrySet())
        {
            if(getRegNrFromCellNr(pair.getKey()) == target)
            {
                result.put(pair.getKey(), new ArrayList<Integer>(pair.getValue()));
            }
        }
        return result;
        */
        /* NOG NIEUWERE METHODE 
        int target = getRegNrFromCellNr(c);
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        for(int i=11; i<100; i++)
        {
            if(Sudoku.REGS[i] == target)
            {
                result.put(i, new ArrayList<Integer>(this.assignment.get(i)));
                if(result.size() == 9) break;
            }
        }
        return result;
        */
        int target = getRegNrFromCellNr(c);
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        int[] cells = REGS2[target];
        for(int i : cells)
        {
            result.put(i, new ArrayList<Integer>(this.assignment.get(i)));
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
        /*for(ArrayList<Integer> vs : range.values())
        {
            for(int i : vs)
            {
                if(i == v && vs.size() == 1) { count++; }
            }
        }*/
        for(ArrayList<Integer> vs : range.values())
        {
            if(vs.contains(v) && vs.size() == 1) { count++; }
            if(count > 1) return false;
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
        
        //int count = 0;
        /*for(ArrayList<Integer> vs : range.values())
        {
            for(int i : vs)
            {
                if(i == v && vs.size() == 1) { return true; }
            }
        }*/
        for(ArrayList<Integer> vs : range.values())
        {
            if(vs.contains(v) && vs.size() == 1) { return true; }
        }
        return false;
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
        //return (int) Math.floor(c / 10);
        return Sudoku.ROWS[c];
    }

    /**
     * Haalt het colnr uit een cellnr
     * c cellnr
     */
    int getColNrFromCellNr(int c)
    {
         //return c % 10;
        return Sudoku.COLS[c];
    }
    
    int getRegNrFromCellNr(int c)
    {
        return Sudoku.REGS[c];
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
        */
        ArrayList<Integer> row = getAssignedInGeneral2(getRow2(c));
        ArrayList<Integer> col = getAssignedInGeneral2(getCol2(c));
        ArrayList<Integer> reg = getAssignedInGeneral2(getReg2(c));
        
        return 0 + row.size() + col.size() + reg.size();

        /*alternatief: tjek vanuit assigned values
        int result = 0;
        HashMap<Integer, ArrayList<Integer>> ass = getAssignedVariables();
        for(Map.Entry<Integer,ArrayList<Integer>> pair : ass.entrySet()){
            int cell = pair.getKey();
            // mis nog wel wat van de region nu
            if(getColNrFromCellNr(cell)==getColNrFromCellNr(c)||getRowNrFromCellNr(cell)==getRowNrFromCellNr(c)){
                result ++;
            }
        }
        return result;*/
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
        if(Solver.REVISE || Solver.HIDDENSINGLES || Solver.HIDDENPAIRS || Solver.NAKEDPAIRS)
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
                    //COUNT EFFECTIVENESS
                    count_revise += toRemove.size();

                    for(int i : toRemove)
                    {
                        values.remove(values.indexOf(i));
                    }
                }
                if(values.size()==0){ //Sudoku is faulty, stop now
                    return false;//will also stop the while if false.
                }

                //HIDDEN SINGLE:
                 //Don't use techniques on newly created givens
                if(Solver.HIDDENSINGLES && values.size() > 1)
                {
                    if(hiddenSingle(c,values)){
                        delete = true; //revise did something
                        //makes values consistent again
                        values = getCell(c);
                    }
                }
                if(Solver.NAKEDPAIRS && values.size() > 1)
                {
                    if(nakedPairs(c,values)){
                        delete = true;
                        values = getCell(c);
                    }
                }
                if(Solver.HIDDENPAIRS && values.size() > 1)
                {
                    if(hiddenPairs(c,values)){
                        delete = true;
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
        difRow.removeAll(otherValuesRow);

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
            //COUNT EFFECTIVENESS
            count_hSingle += values.size()-1;
        }
        else if(difCol.size() == 1 && !difCol.equals(values)){ //whoehoe! hidden single
            setCell(cellNr,difCol);
            delete = true;
            //COUNT EFFECTIVENESS
            count_hSingle += values.size()-1;
        }
        else if(difReg.size() == 1 && !difReg.equals(values)){ //whoehoe! hidden single
            setCell(cellNr,difReg);
            delete = true;
            //COUNT EFFECTIVENESS
            count_hSingle += values.size()-1;
        }
        else if(difRow.size() > 1 || difCol.size() > 1 || difReg.size() > 1){
            //sudoku is wrong :O
        }
        
        return delete;
    }
    
    /**
     * Poging tot Naked Pairs
     * http://www.learn-sudoku.com/naked-pairs.html
     * Ook wel locked sets :
     * http://www.brainbashers.com/sudokulockedsets.asp
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
                    //COUNT EFFECTIVENESS
                    int voor = newvalues.size();
                    boolean changed = newvalues.removeAll(values);
                    //COUNT EFFECTIVENESS
                    count_nPair += voor - newvalues.size();
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

        if(values.size()<=2){
            return delete;
        }

        // pak row van deze cell
        HashMap<Integer, ArrayList<Integer>> row = getNotAssignedInRow(cellNr); 
        // zoek pairs met deze cell
        boolean rowresult = hiddenPairsGeneral(cellNr, values, row);
        // There can be only one pair (then values <= 2, so we have to return)
        if(rowresult){
            //System.out.println("rowresult");
            return rowresult;
        }

        HashMap<Integer, ArrayList<Integer>> col = getNotAssignedInCol(cellNr);
        boolean colresult = hiddenPairsGeneral(cellNr, values, col);
        if(colresult){
            //System.out.println("colresult");
            return colresult;
        }
        
        HashMap<Integer, ArrayList<Integer>> reg = getNotAssignedInReg(cellNr);
        boolean regresult = hiddenPairsGeneral(cellNr, values, reg);
        if(regresult){
            //System.out.println("regresult");
            return regresult;
        }
        // als iets verwijderd is
        delete = rowresult || colresult || regresult;
        return delete;
    }
    
    // niet cell weghalen uit part!
    // TODO fix hiddenPairs :'( als je m met hiddenPairs doet krijgt ie de 2e nieteens af!
    boolean hiddenPairsGeneral(int cellNr, ArrayList<Integer> values, HashMap<Integer, ArrayList<Integer>> part)
    {
        boolean delete = false;
        
        // We hebben de huidige cell met zn values:
        int currentCell = cellNr;
        ArrayList<Integer> currentValues = values;

        //Nu maken we een Map 'inverse', met daarin een ArrayList voor elke value van huidige cell
        HashMap<Integer, ArrayList<Integer>> inverse = new HashMap<Integer, ArrayList<Integer>>();
        for(int j : currentValues) // Voor elke value van de huidige cell
        {
            inverse.put(j, new ArrayList<Integer>()); //Add lijst met als key de huidige value
        }
        //Dan gaan we loopen over de meegegeven part van de sudoku (row/col/reg).
        //Pair wordt de entry van een cell uit dit part van de sudoku (cellNr en values).
        for(Map.Entry<Integer, ArrayList<Integer>> pair : part.entrySet())
        {
            //Als de cell uit de row/col/reg de huidige is, slaan we deze even over
            if(pair.getKey() == currentCell){
                continue;
            }
            for(int i : pair.getValue()) //for every value van de cell uit part
            {
                //System.out.println(currentValues+" contains "+i+" = "+currentValues.contains(i));
                if(currentValues.contains(i)){ //als deze value ook voorkomt in de huidige cell
                    inverse.get(i).add(pair.getKey()); //add cellNr to bijbehorende arraylist
                }
            }
        }

        /**
         * Nu hebben we de map inverse met voor elke waarde van de huidige cell
         * een lijst met andere cells in de huidige range die ook die waarde hebben
         */
        //Initiate a new list to store those values that are present in only one other cell
        ArrayList<Integer> valuesWithOneCell = new ArrayList<Integer>();
        ArrayList<Integer> cell = new ArrayList<Integer>();
        //For every value from our current cell:
        for(Map.Entry<Integer, ArrayList<Integer>> pair : inverse.entrySet())
        {
            int value = pair.getKey(); // A value from our current cell
            int nrOtherCells = pair.getValue().size(); // Number of other cells with that value
            if(nrOtherCells == 1){ // If there is only one other cell with that value
                valuesWithOneCell.add(value); //We store this value
                if(!cell.contains(pair.getValue().get(0))){
                    cell.add(pair.getValue().get(0)); //And the other's cellNr
                }
            }
        }

        // If there were exactly two values also in one other cell, this must be a hidden pair
        if(valuesWithOneCell.size() == 2 && cell.size() == 1){
            //hidden pair!
                    /*System.out.println("*******Hidden Pair overview*********");
                    System.out.println("Current Cell: "+cellNr+" ,with values: "+getCell(cellNr));
                    System.out.println("Part we are looking at: "+part);
                    System.out.println("Inverse created: "+inverse);
                    System.out.println("Chosen values: "+valuesWithOneCell);
                    System.out.println("Chosen cells: "+cell+" ,with values: "+getCell(cell.get(0)));
                    System.out.println("Now we apply the operation: ");
                    System.out.println("before: "+this.assignment);*/

            setCell(currentCell,valuesWithOneCell); // Remove all other options from this cell
            setCell(cell.get(0),new ArrayList<Integer>(valuesWithOneCell)); // And from the other cell
                    
                    /*System.out.println("after: "+this.assignment);
                    System.out.println("Current Cell: "+cellNr+" ,with values: "+getCell(cellNr));
                    System.out.println("Chosen cells: "+cell+" ,with values: "+getCell(cell.get(0)));
                    System.out.println("************************************");*/

            //COUNT EFFECTIVENESS
            int voor = currentValues.size()+getCell(cell.get(0)).size();
            //COUNT EFFECTIVENESS
            count_hPair = voor - (getCell(currentCell).size()+getCell(cell.get(0)).size());

            return true;
        }

        //TODO maybe check for hiddentriples?


        /*
        // map met per value de cells waar de value in voor komt
        // Initiate an arraylist for every of the 9 possible values
        HashMap<Integer, ArrayList<Integer>> inverse = new HashMap<Integer, ArrayList<Integer>>();
        for(int i : new int[] {1,2,3,4,5,6,7,8,9})
        {
            inverse.put(i, new ArrayList<Integer>());
        }

        //For every 'cell = value' pair in part
        for(Map.Entry<Integer, ArrayList<Integer>> pair : part.entrySet())
        {
            for(int i : pair.getValue()) //for every value in that cell
            {
                //add the key to arraylist of that value
                inverse.get(i).add(pair.getKey());
            }
        }
        System.out.println(inverse);

        /*For every arraylist in inverse (for values 1 .. 9)
        for(Map.Entry<Integer, ArrayList<Integer>> pair : inverse.entrySet())
        {
            //retrieve the cellkeys that contain the current value pair.getKey()
            ArrayList<Integer> p1 = pair.getValue(); // 1 => 11,12
            System.out.println("Pair Value: "+pair.getKey());
            System.out.println("Pair Cells: "+p1);

            //For every arraylist in inverse (for values 1 .. 9)
            for(Map.Entry<Integer, ArrayList<Integer>> otherpair : inverse.entrySet())
            {
                //if looking at the same set, skip it
                if(pair.getKey()==otherpair.getKey()){
                    continue;
                }

                //retrieve the cellkeys that contain the current value otherpair.getKey()
                ArrayList<Integer> p2 = otherpair.getValue(); // 2 => 11,12
                System.out.println("OtherPair Value: "+otherpair.getKey());
                System.out.println("OtherPair Cells: "+p2);

                //If these 2 sets contain the same cells, they could be a hidden pair
                if(p1.containsAll(p2) && p2.containsAll(p1))
                {
                    System.out.println("The same cells!");

                    //Now check how many have the same cells
                    //To be a hidden pair, we need: 2 values in 2 same cells
                    ArrayList<Integer> hcells = new ArrayList<Integer>(pair.getValue()); // 11,12
                    ArrayList<Integer> hvalues = new ArrayList<Integer>();
                    
                    for(Map.Entry<Integer, ArrayList<Integer>> ppair : part.entrySet())
                    {
                        Integer pcell = ppair.getKey();
                        ArrayList<Integer> pvalues = ppair.getValue();
                        
                        if(!hcells.contains(pcell)) // niet deel van hidden pair
                        {
                            //COUNT EFFECTIVENESS
                            int voor = pvalues.size();
                            boolean changed = pvalues.removeAll(hvalues);
                            //COUNT EFFECTIVENESS
                            count_hPair = voor - pvalues.size();
                            if(changed) delete = true;
                        }
                    }
                }
            }
        }*/
        
        return delete;
    }
    /**
     * Revise2, less repeating, more to the point!
     * New point:
     * Don't just ask 'did you?' but ask 'what did you do?'.
     * Showing more interest is always a plus.
     * @return ArrayList<Integer> : what cells have been revised?
     */
    ArrayList<Integer> revise2(ArrayList<Integer> cells){
        boolean delete = false;
        ArrayList<Integer> revised = new ArrayList<Integer>(); //what did we revise?
        if(Solver.REVISE || Solver.HIDDENSINGLES || Solver.HIDDENPAIRS || Solver.NAKEDPAIRS)
        {
            for(int c : cells)
            {
                //int c = pair.getKey(); //cellNr
                ArrayList<Integer> values = getCell(c); //possible values

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
                    //COUNT EFFECTIVENESS
                    count_revise += toRemove.size();
                    
                    for(int i : toRemove)
                    {
                        values.remove(values.indexOf(i));
                    }
                    setCell(c,values); //Once again needed!

                }
                if(values.size()==0){ //Sudoku is faulty, stop now
                    break;//will also stop the while if false.
                }

                 //HIDDEN SINGLE:
                 //Don't use techniques on newly created givens
                if(Solver.HIDDENSINGLES && values.size() > 1)
                {
                    if(hiddenSingle(c,values)){
                        delete = true; //revise did something
                        //makes values consistent again
                        values = getCell(c);
                    }
                }
                if(Solver.NAKEDPAIRS && values.size() > 1)
                {
                    if(nakedPairs(c,values)){
                        delete = true;
                        values = getCell(c);
                    }
                }
                if(Solver.HIDDENPAIRS && values.size() > 1)
                {
                    if(hiddenPairs(c,values)){
                        delete = true;
                    }
                }
                
                if(delete){//if something has been removed
                    //The cell has been revised:
                   revised.add(c);
                }
            }
        }
        //returned of revise iets gedaan heeft, is handig in een while.
        return revised;
    }

    /**
     * Return all cells that could be affected by changes in the given cells
     * @param cells
     * @return
     */
    ArrayList<Integer> affected(ArrayList<Integer> cells){
        ArrayList<Integer> returnset = new ArrayList<Integer>();

        for(int c : cells){ //Check all cells individually:
/*
            //Retrieve all unassigned cells in current cell's row/col/reg:
            HashMap<Integer, ArrayList<Integer>> row = getNotAssignedInRow(c);
            HashMap<Integer, ArrayList<Integer>> col = getNotAssignedInCol(c);
            HashMap<Integer, ArrayList<Integer>> reg = getNotAssignedInReg(c);
            //Add these to the returnset
            returnset.addAll(row.keySet());
            returnset.addAll(col.keySet());
            returnset.addAll(reg.keySet());
    */
   
            //Retrieve all unassigned cells in current cell's row/col/reg:
            ArrayList<Integer> row = getNotAssignedInGeneral2(getRow2(c));
            ArrayList<Integer> col = getNotAssignedInGeneral2(getCol2(c));
            ArrayList<Integer> reg = getNotAssignedInGeneral2(getReg2(c));
            //Add these to the returnset
            returnset.addAll(row);
            returnset.addAll(col);
            returnset.addAll(reg);
   
        }
    return new ArrayList(new HashSet(returnset));
    }

    /*
     * Updated versie: don't return values
     * Returned de row waar de huidige cell in voorkomt
     * @param c cellNr
     * @return srow lijst met cellNrs uit de row
     */
    ArrayList<Integer> getRow2(int c)
    {

        /* NOG NOG NIEUWERE METHODE 
        int target = getRowNrFromCellNr(c);
        //HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(int i=11; i<100; i++)
        {
            if(Sudoku.ROWS[i] == target)
            {
                //result.put(i, new ArrayList<Integer>(this.assignment.get(i)));
                result.add(i);
                if(result.size() == 9) break;
            }
        }
        return result;
        */
        int target = getRowNrFromCellNr(c);
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(int i : ROWS2[target])
        {
            result.add(i);
        }
        return result;
    }

    /*
     * Updated versie: don't return values
     * Returned de col waar de huidige cell in voorkomt
     * @param c cellNr
     * @return scol lijst met cellNrs uit de col
     */
    ArrayList<Integer> getCol2(int c)
    {
        /* NOG NOG NIEUWERE METHODE 
        int target = getColNrFromCellNr(c);
        //HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(int i=11; i<100; i++)
        {
            if(Sudoku.COLS[i] == target)
            {
                //result.put(i, new ArrayList<Integer>(this.assignment.get(i)));
                result.add(i);
                if(result.size() == 9) break;
            }
        }
        return result;
        */
        int target = getColNrFromCellNr(c);
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(int i : COLS2[target])
        {
            result.add(i);
        }
        return result;
    }

    /**
     * Updated versie: don't return values
     * Returned de 3by3 region waar de huidige cell in voorkomt
     * @param c : cellNr
     * @return sreg : lijst met cellNrs uit de region
     */
    ArrayList<Integer> getReg2(int c){

        /* NOG NOG NIEUWERE METHODE 
        int target = getRegNrFromCellNr(c);
        //HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(int i=11; i<100; i++)
        {
            if(Sudoku.REGS[i] == target)
            {
                //result.put(i, new ArrayList<Integer>(this.assignment.get(i)));
                result.add(i);
                if(result.size() == 9) break;
            }
        }
        return result;
        */
        int target = getRegNrFromCellNr(c);
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(int i : REGS2[target])
        {
            result.add(i);
        }
        return result;
    }

   ArrayList<Integer> getNotAssignedInGeneral2(ArrayList<Integer> context)
    {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(int i : context)
        {
            if(getCell(i).size() > 1)
            {
                result.add(i);
            }
        }
        return result;
    }

   ArrayList<Integer> getAssignedInGeneral2(ArrayList<Integer> context)
    {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(int i : context)
        {
            if(getCell(i).size() == 1)
            {
                result.add(i);
            }
        }
        return result;
    }


	
}
