/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sudokucsp

/**
 *
 * @author Armon
 */
class Sudoku {
    
    def assignment
    def full = [1,2,3,4,5,6,7,8,9] // used in multiple methods as check.
    
    /**
     * Constructor
     * @see textToAssignment
     * @param input textuele representatie
     */
    Sudoku(String input)
    {
        this.assignment = textToAssignment(input)
    }
    
    /**
     * Copy constructor
     */
    Sudoku(Sudoku s)
    {
        this.assignment = s.assignment.clone()
    }
    
    def textToAssignment(input)
    {
        // input: .94...13..............76..2.8..1.....32.........2...6.....5.4.......8..7..63.4..8
        // resultaat: [11:1..9, 12:[9], 13:[4], etc.
        
        input = input.trim()

        def assignment = [:];

        int row = 1;
        int col = 1;
        for(i in input)
        {
            def values;

            def cell = row*10+col;

            if(i == ".")
            {
                values = 1..9;
            }
            else
            {
                values = [ Integer.parseInt(i) ];
            }

            assignment[(cell)] = values;

            col++
            if(col > 9)
            {
                col = 1
                row++
            }
        }

        return assignment;
    }
    
    String toString()
    {
        def sb = ""
        for(i in 1..9)
        {
            for(j in 1..9)
            {
                def x
                if(this.assignment[i*10+j].size() > 1)
                {
                    x = "."
                }
                else
                {
                    x = this.assignment[i*10+j][0]
                }
                sb = sb+x
            }
        }
        return sb
    }
    
    def getCell(c)
    {
        return this.assignment[c];
    }
    
    def setCell(c, vs)
    {
        this.assignment[c] = vs;
    }    

    /**
     * Kijkt of de sudoku consistent is:
     * (1) elke waarde is uit 1..9
     * (2) elke waarde die assigned is mag maar 1x voorkomen in zijn rij en kolom
     *
     * @return consistent of niet
     */
    def isConsistent()
    {
        // (1)
        def one = this.assignment.every{
            full.containsAll(it.value) && it.value.size() > 0
        }
        // (2)
        // pak alle cells die assigned zijn
        //def assigned = this.assignment.findAll{ it.value.size() == 1 }
        def assigned = getAssignedVariables() // wel gebruik maken van methods he
        // voor elke assigned cell
        def two = assigned.every{
            // kijk of ie 1x voorkomt in zijn row en column
            onlyAppearsOnceInRow(it[1], it[0]) && 
            onlyAppearsOnceInColumn(it[1], it[0]) &&
            onlyAppearsOnceIn3by3(it[1],it[0])
        }

        return one && two
    }
    
    /**
     * Geeft de eerste variabele die nog niet assigned is
     * oftewel een lijst (size > 1) met mogelijke waarden heeft
     * @param incomplete assignment
     * @return lijst met 1 element, cellnr
     */
    def getNotAssignedVariables()
    {
        def result = []
        for(pair in this.assignment)
        {
            if(pair.value.size() > 1)
            {
                result << [pair.key, pair.value]
            }
        }
        return result
    }
    
    /**
     * Geeft een lijst met cells die assigned zijn en hun waarde
     * @param assignment (in)complete assignment
     * @return lijst met lijst per cel, bijv: [ [ 11, [1] ], [ 63, [5] ] ]
     */
    def getAssignedVariables()
    {
        def result = []
        for(pair in this.assignment)
        {
            if(pair.value.size() == 1)
            {
                result << [pair.key, pair.value]
            }
        }
        return result
    }

    /**
     * Geeft een lijst met cells die assigned zijn in de row en hun waarde
     * @param c : cell number
     * @return lijst met lijst per cel, bijv: [ [ 11, [1] ], [ 12, [5] ] ]
     */
    def getAssignedInRow(c)
    {
        def result = []
        def srow = getRow(c);
        for(pair in srow){
            if(pair.value.size() == 1)
            {
                result << [pair.key, pair.value]
            }
        }
        return result
    }

    /**
     * Geeft een lijst met cells die niet assigned zijn in de row en hun waarde
     * @param c : cell number
     * @return lijst met lijst per cel, bijv: [ [ 11, [1,2] ], [ 12, [3,5,6] ] ]
     */
    def getNotAssignedInRow(c)
    {
        def result = []
        def srow = getRow(c);
        for(pair in srow){
            if(pair.value.size() > 1)
            {
                result << [pair.key, pair.value]
            }
        }
        return result
    }

    /**
     * Geeft een lijst met cells die assigned zijn in de col en hun waarde
     * @param c : cell number
     * @return lijst met lijst per cel, bijv: [ [ 11, [1] ], [ 21, [5] ] ]
     */
    def getAssignedInCol(c)
    {
        def result = []
        def scol = getCol(c);
        for(pair in scol){
            if(pair.value.size() == 1)
            {
                result << [pair.key, pair.value]
            }
        }
        return result
    }

    /**
     * Geeft een lijst met cells die niet assigned zijn in de col en hun waarde
     * @param c : cell number
     * @return lijst met lijst per cel, bijv: [ [ 11, [1,2] ], [ 21, [1,5] ] ]
     */
    def getNotAssignedInCol(c)
    {
        def result = []
        def scol = getCol(c);
        for(pair in scol){
            if(pair.value.size() > 1)
            {
                result << [pair.key, pair.value]
            }
        }
        return result
    }
    
    /**
     * Geeft een lijst met cells die assigned zijn in de reg en hun waarde
     * @param c : cell number
     * @return lijst met lijst per cel, bijv: [ [ 11, [1] ], [ 21, [5] ] ]
     */
    def getAssignedInReg(c)
    {
        def result = []
        def sreg = getReg(c);
        for(pair in sreg){
            if(pair.value.size() == 1)
            {
                result << [pair.key, pair.value]
            }
        }
        return result
    }

    /**
     * Geeft een lijst met cells die niet assigned zijn in de reg en hun waarde
     * @param c : cell number
     * @return lijst met lijst per cel, bijv: [ [ 11, [1,2] ], [ 21, [1,5] ] ]
     */
    def getNotAssignedInReg(c)
    {
        def result = []
        def sreg = getReg(c);
        for(pair in sreg){
            if(pair.value.size() > 1)
            {
                result << [pair.key, pair.value]
            }
        }
        return result
    }

    /*
     * Returned de row waar de huidige cell in voorkomt
     * @param c cellNr
     * @return srow lijst met cells uit de row
     */
    def getRow(c)
    {
        // pak alle cells in de sudoku in dezelfde rij als cell
        def srow = this.assignment.findAll{ getRowFromCellNr(it.key) == getRowFromCellNr(c) }
    }

    /*
     * Returned de col waar de huidige cell in voorkomt
     * @param c cellNr
     * @return scol lijst met cells uit de col
     */
    def getCol(c)
    {
        // pak alle cells in de sudoku in dezelfde kolom als cell
        def scol = this.assignment.findAll{ getColFromCellNr(it.key) == getColFromCellNr(c) }
    }

    /**
     *TODO Zorgen dat de 3x3 region niet telkens overnieuw hoeft worden berekend
     * Returned de 3by3 region waar de huidige cell in voorkomt
     * @param c : cellNr
     * @return sreg : lijst met cells uit de region
     */
    def getReg(c){
        def rowNr = getRowFromCellNr(c); // Retrieve current row
        def rowReg = rowNr%3 // Calculate relative row number (in a region of 3by3)
        def rows = [];
        def colNr = getColFromCellNr(c); // Retrieve current column
        def colReg = colNr%3 // Calculate relative col number (in a region of 3by3)
        def cols = [];

        //determine other rows
        if(rowReg == 1){
            def row1 = rowNr + 1;
            def row2 = rowNr + 2;
            rows = [rowNr,row1,row2]; // Current region in rows
        } else if(rowReg == 2){
            def row1 = rowNr - 1;
            def row2 = rowNr + 1;
            rows = [row1, rowNr, row2]; // Current region in rows
        } else if(rowReg == 0){
            def row1 = rowNr - 1;
            def row2 = rowNr - 2;
            rows = [row2, row1, rowNr]; // Current region in rows
        }

        //determine other columns
        if(colReg == 1){
            def col1 = colNr + 1;
            def col2 = colNr + 2;
            cols = [colNr,col1,col2]; // Current region in columns
        } else if(colReg == 2){
            def col1 = colNr - 1;
            def col2 = colNr + 1;
            cols = [col1, colNr, col2]; // Current region in columns
        } else if(colReg == 0){
            def col1 = colNr - 1;
            def col2 = colNr - 2;
            cols = [col2, col1, colNr]; // Current region in columns
        }

        // Retrieve all cells that fit in these col/row constraints
        def sreg = this.assignment.findAll{
            cols.contains(getColFromCellNr(it.key)) &&
            rows.contains(getRowFromCellNr(it.key))
        }

        return sreg           
    }
    
    /**
     * Kijkt of waarde x eenmaal voorkomt in de row van cell
     * @param v waarde
     * @param c cell
     */
    def onlyAppearsOnceInRow(v, c)
    {
        // pak alle cells in de sudoku in dezelfde rij als cell
        //def srow = this.assignment.findAll{ getRowFromCellNr(it.key) == getRowFromCellNr(c) }
        def srow = getRow(c)
        // controleer of x 1x voorkomt in deze cells
        def result = onlyAppearsOnceInRange(v, srow)
    }

    /**
     * Kijkt of waarde x eenmaal voorkomt in de column van cell
     * @param v waarde
     * @param c cell
     */
    def onlyAppearsOnceInColumn(v, c)
    {
        // pak alle cells in de sudoku in dezelfde kolom als cell
        //def scol = this.assignment.findAll{ getColFromCellNr(it.key) == getColFromCellNr(c) }
        def scol = getCol(c)
        // controleer of x 1x voorkomt in deze cells
        def result = onlyAppearsOnceInRange(v, scol)
    }

    /**
     * Kijkt of waarde eenmaal voorkomt in een 3by3 region van de cell
     * @param v waarde
     * @param c cellnr
     */
    def onlyAppearsOnceIn3by3(v,c){
        // pak alle cells in de sudoku in dezelfde region als cell
        def sreg = getReg(c)
        // controleer of v 1x voorkomt in deze cells
        def result = onlyAppearsOnceInRange(v,sreg)
    }

    /**
     * Kijkt of waarde x eenmaal of niet voorkomt in een groepje cells
     * v waarde
     * range groepje cells (deel van een Map)
     */
    def onlyAppearsOnceInRange(v, range)
    {
        def result = range.values().toList().count(v) <= 1
    }
    
    /**
     * Kijkt of waarde x voorkomt in een groepje cells
     * v waarde
     * range groepje cells (deel van een Map)
     */
    def appearsInRange(v, range)
    {
        def result = range.values().toList().count(v) >= 1
    }

    /**
     * Kijkt of waarde v in c inconsistentie zou geven
     * @param v     : value voor de cell
     * @param c     : cell number
     * @return  boolean : true -> consistent ; false -> inconsistent
     */
    def cellConsistent(v,c){
        // pak alle cells in de sudoku in dezelfde kolom als cell
        def scol = getCol(c)
        scol.remove(c)
        // pak alle cells in de sudoku in dezelfde rij als cell
        def srow = getRow(c)
        srow.remove(c)
        // pak alle cells in de sudoku in dezelfde region als cell
        def sreg = getReg(c)
        sreg.remove(c)
        return !appearsInRange(v,scol) && !appearsInRange(v,srow) && !appearsInRange(v,sreg);
    }

    /**
     * Haalt het rownr uit een cellnr
     * c cellnr
     */
    def getRowFromCellNr(c)
    {
        def row = (int) Math.floor(c / 10)
    }

    /**
     * Haalt het colnr uit een cellnr
     * c cellnr
     */
    def getColFromCellNr(c)
    {
         def col = c % 10
    }

    /**
     *TODO zorgen dat revise/hidden single de eerste stap '7' maakt.
     * Hiervoor zal het nodig zijn om de hele sudoku te checken? I dunno
     * Ik heb al gezien in mijn eigen sudoku programma dat onze eerste sudoku
     *het volgende heeft:
     *[25678],9,4,[58],[28],[25],1,3,[56]
     *Hidden Single zou hier de 7 aangeven als eerste getalletje, wat heel
     *veel backtracking zal schelen!
     *
     * Revise:
     * Zorgt dat binary constraints voldaan zijn, i.e. delete impossible values
     * @param c     :   cell number to be revised
    */
    def revise(c){
        def delete = false; //Did we delete something?
        def values = this.assignment[c] //obtain possible values
        // NORMAL REVISE:
        for(v in values){//for each possible value of cell
            if(!cellConsistent([v],c)){//if value is not consistent with sudoku
                values = values - [v] //remove value
                delete = true;
                if(values.size() == 0){
                    break
                }
            }
        }
        //HIDDEN SINGLE:
        if(hiddenSingle(c,values)){
            //println 'hidden'
            delete = true //revise did something
        }

        //update mogelijke values in cell
        setCell(c,values)
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
    */
    def openSingles() {
       for(i in 1..9){
           /*
            * Check every row for open singles
           */
           def inRow = getAssignedInRow(i) //all assigned values & their keys
           def keyR = []
           def valueR = []
           if(inRow.size()==8){ //if only one value is missing
 // e.g. [ [11,1], [12,2], [13,3], [14,4], [15,5], [16,6], [17,7], [19,8]]
               inRow.each{ //for every given value in the list
                   keyR << getColFromCellNr(it[0]) //retrieve the col in row
                   valueR << it[1] //retrieve the value
               }
               def missingValueR = full - valueR //one value will remain
               def missingKeyR = full - keyR// one column will remain
               missingKeyR = missingKeyR[0]+10*i //assemble cell key
               this.assignment[missingKeyR] = missingValueR //assign the value
           }

           /*
            * Check every column for open singles
           */
           def inCol = getAssignedInCol(i) //all assigned values & their keys
           def keyC = []
           def valueC = []
           if(inCol.size()==8){ //if only one value is missing
 // e.g. [ [11,1], [21,2], [31,3], [41,4], [51,5], [61,6], [71,7], [91,8]]
               inCol.each{ //for every given value in the list
                   keyC << getRowFromCellNr(it[0]) //retrieve the row in col
                   valueC << it[1] //retrieve the value
               }
               def missingValueC = full - valueC //one value will remain
               def missingKeyC = full - keyC// retrieve empty col in row
               missingKeyC = missingKeyC[0]+10*i //assemble cell key
               this.assignment[missingKeyC] = missingValueC //assign the value
           }
       }

   }

    /*
     *TODO hiddenSingle ook toepassen op columns en regions
     *Hidden Singles or Pinned Squares
     *Hidden: http://www.learn-sudoku.com/hidden-singles.html
     *Pinned: http://www.brainbashers.com/sudokupinnedsquares.asp
     *If list of values in the cell contains a unique value for its row,col
     *then that is a Hidden Single and we can assign that value to the cell.
     *@param cellNr : nr of the cell
     *@return values : (possibly) new values for the cell
    */

    def hiddenSingle(cellNr,values) {
        def delete = false; // bijhouden of we iets deleten

        //ALL: Intialize; Retrieve other cells
        def nrow = getNotAssignedInRow(cellNr) //retrieve oningevulde rowcells
        def row = nrow - [[cellNr, values]] //haal huidige er vanaf

        def ncol = getNotAssignedInCol(cellNr) //retrieve oningevulde rowcells
        def col = ncol - [[cellNr, values]] //haal huidige er vanaf

        def nreg = getNotAssignedInReg(cellNr) //retrieve oningevulde rowcells
        def reg = nreg - [[cellNr, values]] //haal huidige er vanaf

        //ALL: combine values from the other cells, compare with current values
        //ROW: Now put all the values from the other cells in one list
        def otherValuesRow = []
        row.each{
            otherValuesRow = otherValuesRow + it[1]
            otherValuesRow.unique()
        }
        //check what values are in this cell, but not in the others
        def difRow = values - otherValuesRow

        //COL: Now put all the values from the other cells in one list
        def otherValuesCol = []
        col.each{
            otherValuesCol = otherValuesCol + it[1]
            otherValuesCol.unique()
        }
        def difCol = values - otherValuesCol

        //REG: Now put all the values from the other cells in one list
        def otherValuesReg = []
        reg.each{
            otherValuesReg = otherValuesReg + it[1]
            otherValuesReg.unique()
        }
        def difReg = values - otherValuesReg

        //ALL: now check if they are singles (and changed)
        if(difRow.size() == 1 && difRow != values){ //whoehoe! hidden single
            setCell(cellNr,difRow);
            delete = true;
        }
        else if(difCol.size() == 1 && difCol != values){ //whoehoe! hidden single
            setCell(cellNr,difCol);
            delete = true;
        }
        else if(difReg.size() == 1 && difReg != values){ //whoehoe! hidden single
            setCell(cellNr,difReg);
            delete = true;
        }
        else if(difRow.size() > 1 || difCol.size() > 1 || difReg.size() > 1){
            println 'faulty sudoku values'
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
            println difReg

            //sudoku is wrong, print debug stuff
        }
        
        return delete
    }
	
}

