package gol.Model.Board;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Momcilo Delic on 4/8/2017.
 *
 * The class implements an Interface
 * This will make it easier for me to switch between the classes and methods
 * in my controller.
 */

public class DynamicBoard implements InterfaceBoard {

    //=========================================================================//
    //                             @FXML                                       //
    //=========================================================================//

    @FXML public ColorPicker                cellColor;
    @FXML public ColorPicker                backgroundColor;
    @FXML public ColorPicker                gridColor;


    //=========================================================================//
    //                             Variabler                                   //
    //=========================================================================//
    private int height, width;
    private int                             movedistance = 1;
    private double                          cellHeight, cellWidth, cellSize;
    private ArrayList<ArrayList<Byte>>      board, patternExtend;
    private GraphicsContext                 gc;
    private boolean                         circle;
    private boolean                         dynamicSize;



    /**
     * The constructor of the class DynamicBoard, with parameters
     * @param gc GraphicsContext
     * @param cellSize The cellSize
     * @param width,height The width and the height
     */
    public DynamicBoard(GraphicsContext gc, double cellSize, int width, int height){
        this.cellSize = cellSize;
        this.gc = gc;
        this.height = width;
        this.width = height;
        newBoard();
        dynamicSize = true;
    }

    /**
     * Making an empty board, adding byte 0's to that empty board
     *
     * @author Momcilo Delic - s315282
     */
    private void newBoard(){
        board = new ArrayList<>();
        for(int i = 0; i < height; i++){
            board.add(new ArrayList<>());
            for (int j = 0; j < width; j++) {
                board.get(i).add((byte)0);
            }
        }
    }

    /**
     * This is a setLive method for my DynamicBoard
     * I'm using an exact method in my StaticBoard class
     * This makes it easier for me when implementing methods
     * through my Interface. For instance in my draw method in the
     * Controller when i call setLive it can go both ways.
     * @param x gets the x
     * @param y gets the y
     * @param state sets the state of the cell in byte
     *
     * @author Momcilo Delic - s315282
     */
    @Override
    public void setLive(int x, int y, byte state) {
        board.get(x).set(y, state);
    }

    /**
     * Getter for my height
     * @return returns the board height
     * @author Momcilo Delic - s315282
     */
    @Override
    public int getWidth() {
        return board.get(0).size();
    }

    /**
     * Getter for my width
     * @return returns the board width
     * @author Momcilo Delic - s315282
     */
    @Override
    public int getHeight() {
        return board.size();
    }

    /**
     * @param x Gets the x
     * @param y Gets the y
     * @return returns the board x and y (equivalent to board[x][y] in Static Board)
     * @author Momcilo Delic - s315282
     */
    @Override
    public byte getLive(int x, int y) {
        return board.get(x).get(y);
    }

    /**
     * setter for the boolean circle
     * this boolean is used on a check button
     * @param circle If the checkButton is selected, the set circle will be set to true
     * @author Momcilo Delic - s315282
     */
    public void setCircle(boolean circle){
        this.circle = circle;
    }

    /**
     * setter for boolean dynamicSize
     * this boolean is used for dynamic game Sizing
     * @param dynamicSize If the checkButton is selected, the set dynamicSize will be set to true
     * @author Momcilo Delic - s315282
     */
    public void setDynamicSize(boolean dynamicSize){
        this.dynamicSize = dynamicSize;
    }


    /**
     * The draw method that draws the game
     * setFill from the getValue color of the Color Picker for the backGround
     * setFill from the getValue color of the Color Picker for the cellColor
     *
     * If boolean dynamicSize is true cellSize will change to cellHeight/cellWidth
     *
     * If boolean circle is true the shape of the cells will change to circles
     *
     * Moving my drawGrid method to my draw Method
     *
     * @author Momcilo Delic - s315282
     */
    public void draw() {
        gc.clearRect(0, 0, getCanvasHeight(), getCanvasWidth());
        gc.setFill(backgroundColor.getValue());
        gc.fillRect(0, 0, getCanvasHeight(), getCanvasWidth());
        gc.setFill(cellColor.getValue());
        if(dynamicSize) {
            cellHeight = getCanvasHeight() / getWidth();
            cellWidth = getCanvasWidth() / getHeight();
            if (cellWidth < cellHeight) {
                cellSize = cellWidth;
            } else {
                cellSize = cellHeight;
            }
        }
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                if (getLive(i, j) == 1) {
                    if(!circle) {
                        gc.fillRect(cellSize * j, cellSize * i, cellSize, cellSize);
                    } else {
                        gc.fillOval(cellSize * j, cellSize * i, cellSize, cellSize);
                    }
                }
            }
        }
        drawGrid();
    }

    /**
     * Making an empty ArrayList,
     * Checking where getLive is == 1
     * gets x and subtracts with movedistance (movedistance = 1) and sets the bytes to 1
     * Since it subtract x, the pattern will move up
     *
     * @author Momcilo Delic - s315282
     */
    @Override
    public void patternUp() {
        addTop();
        patternExtend = new ArrayList<>();
        for (int y = 0; y < board.size(); y++) {
            ArrayList<Byte> row = new ArrayList<>();
            for (int x = 0; x < board.get(y).size(); x++)
                row.add((byte) 0);
            patternExtend.add(row);
        }
        for (int x = 0; x < board.size(); x++) {
            for (int y = 0; y < board.get(x).size(); y++) {
                if (getLive(x, y) == 1)
                    patternExtend.get(x - movedistance).set(y, (byte) 1);
            }
        }
        board = patternExtend;
        draw();
    }

    /**
     * Making an empty ArrayList,
     * Checking where getLive is == 1
     * gets x and adds with movedistance (movedistance = 1) and sets the bytes to 1
     * Since it adds x, the pattern will move down
     *
     * @author Momcilo Delic - s315282
     */
    @Override
    public void patternDown() {
        addDown();
        patternExtend = new ArrayList<>();
        for (int y = 0; y < board.size(); y++) {
            ArrayList<Byte> row = new ArrayList<>();
            for (int x = 0; x < board.get(y).size(); x++)
                row.add((byte) 0);
            patternExtend.add(row);
        }
        for (int x = 0; x < board.size(); x++) {
            for (int y = 0; y < board.get(x).size(); y++) {
                if (getLive(x, y) == 1)
                    patternExtend.get((x + movedistance)).set(y, (byte) 1);
            }
        }
        board = patternExtend;
        draw();
    }

    /**
     * Doing the exact same like in the other pattern methods
     * only here i substract y with movedistance
     *
     * @author Momcilo Delic - s315282
     */
    @Override
    public void patternLeft() {
        addLeft();
        patternExtend = new ArrayList<>();
        for (int y = 0; y < board.size(); y++) {
            ArrayList<Byte> row = new ArrayList<>();
            for (int x = 0; x < board.get(y).size(); x++)
                row.add((byte) 0);
            patternExtend.add(row);
        }
        for (int x = 0; x < board.size(); x++) {
            for (int y = 0; y < board.get(x).size(); y++) {
                if (getLive(x, y) == 1)
                    patternExtend.get(x).set(y - movedistance, (byte) 1);
            }
        }
        board = patternExtend;
        draw();
    }

    /**
     * Doing the exact same like in the other pattern methods
     * only here i add y with movedistance
     *
     * @author Momcilo Delic - s315282
     */
    @Override
    public void patternRight() {
        addRight();
        patternExtend = new ArrayList<>();
        for (int y = 0; y < board.size(); y++) {
            ArrayList<Byte> row = new ArrayList<>();
            for (int x = 0; x < board.get(y).size(); x++)
                row.add((byte) 0);
            patternExtend.add(row);
        }
        for (int x = 0; x < board.size(); x++) {
            for (int y = 0; y < board.get(x).size(); y++) {
                if (getLive(x, y) == 1)
                    patternExtend.get(x).set(y + movedistance, (byte) 1);
            }
        }
        board = patternExtend;
        draw();
    }

    /**
     * Set the game board, converts from byte to array
     * @param newGameBoard getting a byte and adding the byteboard to an arraylist
     *
     * @author Momcilo Delic - s315282
     */
    @Override
    public void setgameBoard(byte[][] newGameBoard) {
        this.height = newGameBoard.length;
        this.width = newGameBoard[0].length;
        this.board = new ArrayList<>();
        for (int i = 0; i < newGameBoard.length; i++) {
            board.add(new ArrayList<>());
            for (int j = 0; j < newGameBoard[i].length; j++) {
                board.get(i).add(newGameBoard[i][j]);
            }
        }
    }

    public void setgameBoard(ArrayList<ArrayList<Byte>> newBoard) {
        this.height = newBoard.size();
        this.width = newBoard.get(0).size();
        this.board = new ArrayList<>();
        for (int i = 0; i < newBoard.size(); i++) {
            board.add(new ArrayList<>());
            for (int j = 0; j < newBoard.get(i).size(); j++) {
                board.get(i).add(newBoard.get(i).get(j));
            }
        }
    }


    /**
     * Next generation, counts neighbours and sets
     * next generation. If there is less than two neighbours
     * the cell dies. Cells with two or three neighbours continues
     * to live. Cells with more than three neighbours die. If a dead
     * cell is surrounded with 3 alive cells, it becomes alive.
     *
     * In addition this board will expand if the cells hit near the corners
     * by adding left/right/top/down
     *
     * @author Momcilo Delic - s315282
     * @param start
     * @param stop
     */

    @Override
    public void nextGeneration(int start, int stop) {
        addLeft();
        addRight();
        addTop();
        addDown();

        ArrayList<ArrayList<Byte>> nextGeneBoard = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            nextGeneBoard.add(new ArrayList<>());
            for (int j = 0; j < width; j++) {
                nextGeneBoard.get(i).add((byte) 0);
            }
        }
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {

                if (countNeighbours(x, y) == 3) {
                    nextGeneBoard.get(x).set(y, (byte) 1);
                } else if (getLive(x, y) == 1 && countNeighbours(x, y) == 2) {
                    nextGeneBoard.get(x).set(y, (byte) 1);
                } else if (countNeighbours(x, y) > 3) {
                    nextGeneBoard.get(x).set(y, (byte) 0);
                } else if (countNeighbours(x, y) < 2) {
                    nextGeneBoard.get(x).set(y, (byte) 0);
                }
            }

        }
        board = nextGeneBoard;
        draw();
    }

    /**
     * Mix it up and spice it up a little bit >:)
     * This is where Game of Life gets dangerous and exciting! (not really)
     *
     * For every cell that has 3 alive neighbours, it will be set alive on next gen
     *
     * @author Momcilo Delic - s315282
     */
    public void fastPopulateRule() {
        addLeft();
        addRight();
        addTop();
        addDown();

        ArrayList<ArrayList<Byte>> nextBoard = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            nextBoard.add(new ArrayList<>());
            for (int j = 0; j < width; j++) {
                nextBoard.get(i).add((byte) 0);
            }
        }
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {

                if (countNeighbours(x,y) == 3){
                    nextBoard.get(x).set(y,(byte)1);
                }
                else {
                    nextBoard.get(x).set(y, board.get(x).get(y));
                }
            }

        }
        board = nextBoard;
        draw();
    }

    /**
     * This is an improvized rule i made, it can make some cool pattern
     * especially if you load and play a file
     * I like to call this the ImprovizedRule a.k.a Epilepsy Attack.
     *
     * If it counts 1 neighbour 1, it will be set 0
     * If it counts 2 neighbours, it will be set to 1
     * If it counts 3 neighbours, it will also be set alive
     *
     * @author Momcilo Delic - s315282
     */
    @Override
    public void ImprovizedRule() {
        addLeft();
        addRight();
        addTop();
        addDown();
        ArrayList<ArrayList<Byte>> nextBoard = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            nextBoard.add(new ArrayList<>());
            for (int j = 0; j < width; j++) {
                nextBoard.get(i).add((byte) 0);
            }
        }
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {

                if (countNeighbours(x, y) == 1) {
                    nextBoard.get(x).set(y, (byte) 0);
                } else if (countNeighbours(x, y) == 2) {
                    nextBoard.get(x).set(y, (byte) 1);
                } else if (countNeighbours(x, y) == 3) {
                    nextBoard.get(x).set(y, (byte) 1);
                }
            }

        }
        board = nextBoard;
        draw();
    }

    /**
     * Converts Array to string
     * This is mainly used to test my methods
     * @return returns Strings
     *
     * @author Momcilo Delic - s315282
     */
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < board.size(); i++){
            for(int j = 0; j < board.get(i).size(); j++){
                stringBuffer.append(board.get(i).get(j));
            }
        }
        return stringBuffer.toString();
    }

    /**
     * Counts the neighbours and using ++ if the statment is true
     * CountNeighbours get used in the nextGeneration method
     * @author Momcilo Delic - s315282
     */
    private int countNeighbours(int x, int y) {
        int cNeighbour = 0;
        if (x > 0) {
            // This will check the neighbours for North
            if (getLive(x - 1, y) == 1)
                cNeighbour++;

            // This will check the neighbours for North/West
            if (y > 0) {
                if (getLive(x - 1,y - 1) == 1)
                    cNeighbour++;
            }

            // This will check the neighbours for North/East
            if (y < width - 1) {
                if (getLive(x - 1,y + 1) == 1)
                    cNeighbour++;
            }
        }

        // This will check the neighbours for West
        if (y > 0) {
            if (getLive(x,y - 1) == 1) {
                cNeighbour++;
            }
        }

        // This will check the neighbours for East
        if (y < width - 1) {
            if (getLive(x,y + 1) == 1)
                cNeighbour++;
        }

        // This will check the neighbours for South
        if (x < height - 1) {
            if (getLive(x + 1, y) == 1) {
                cNeighbour++;
            }
            // This will check the neighbours for South/West
            if (y > 0) {
                if (getLive(x + 1,y - 1) == 1)
                    cNeighbour++;
            }
            // This will check the neighbours for South/East
            if (y < width - 1) {
                if (getLive(x + 1,y + 1) == 1)
                    cNeighbour++;
            }
        }
        return cNeighbour;
    }

    /**
     * Clears the Board and fills the board with 0's
     * When the clear button is pressed, it will set new Board to 45 60
     *
     * TODO: An improvment could be when the user clicks "Clear Button" he gets an option
     * TODO: whether he want to keep the current board size or go back to start size (45, 60)
     * @author Momcilo Delic - s315282
     */
    @Override
    public void ClearButton(){
        board.clear();
        for (int x = 0; x < height; x++) {
            board.add(new ArrayList<>());
            for(int y = 0; y < width; y++){
                board.get(x).add((byte) 0);
            }
        }
        setgameBoard(new byte[45][60]);
    }

    /**
     * Draws the grid
     * @author Momcilo Delic - s315282
     */
    private void drawGrid(){
        // If the cellSize get to 2.5, the grid will turn off
        // The cellSize is set to 2.5 is because thats when the grid basically becomes the background
        if (cellSize > 2.5 ) {

            // Drawing the Horizontal lines
            // Adding + 1 to add one more last line to make the board look like an box
            for (double i = 0; i < board.size() + 1; i++) {
                gc.setStroke(gridColor.getValue());
                gc.strokeLine(0, i * cellSize, board.get(0).size() * cellSize, i * cellSize);
            }

            // Drawing the Vertical Lines
            // Adding + 1 to add one more last line to make the board look like an box
            for (double j = 0; j < board.get(0).size() + 1; j++) {
                gc.setStroke(gridColor.getValue());
                gc.strokeLine(j * cellSize, 0, j * cellSize, board.size() * cellSize);
            }
        }
    }

    /**
     * Adding a new column on left with 0's
     *
     * @author Momcilo Delic - s315282
     */
    public void addLeft() {
        for (int y = 0; y < height; y++) {
            if (board.get(y).get(0) == 1) {
                width++;
                for (int i = 0; i < height; i++) {
                    board.get(i).add(0, (byte) 0);
                }
            }
        }
    }

    /**
     * Adding a new row on top with 0's
     *
     * @author Momcilo Delic - s315282
     */
    public void addTop() {
        for (int x = 0; x < width; x++) {
            if (board.get(0).get(x) == 1) {
                height++;
                ArrayList<Byte> temp = new ArrayList<Byte>();
                for (int i = 0; i < width; i++) {
                    temp.add((byte) 0);
                }
                board.add(0, temp);
            }
        }
    }

    /**
     * Adding new column on the right with 0's
     *
     * @author Momcilo Delic - s315282
     */
    public void addRight(){
        for (int y = 0; y < height; y++) {
            if (board.get(y).get(width - 2) == 1) {
                width++;
                for (int i = 0; i < height; i++) {
                    board.get(i).add((byte) 0);
                }
            }
        }
    }

    /**
     * Adding new rows on the bottom with 0's
     *
     * @author Momcilo Delic - s315282
     */
    public void addDown(){
        int inc = 1;

        for (int x = 0; x < width; x++) {
            if (board.get(height - 2).get(x) == 1) {
                height++;
                board.add(new ArrayList<>());
                for (int i = 0; i < width; i++) {
                    board.get(height - inc).add((byte) 0);
                }
            }
        }
    }

    /**
     * Random generator, randomness set to 2
     *
     * @author Momcilo Delic - s315282
     */
    public void Randomness(){
        Random random = new Random();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board.get(i).set(j,(byte) random.nextInt(2));
            }
        }

    }

    /**
     * Using valueProperty listeners to get
     * Cell color, Grid color and
     * Background color from Users input
     *
     * @author Momcilo Delic - s315282
     */
    @Override
    public void cellColorPicker() {
        cellColor.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                draw();
            }
        });
    }

    /**
     * Using valueProperty listeners to get
     * Cell color, Grid color and
     * Background color from Users input
     *
     * @author Momcilo Delic - s315282
     */
    @Override
    public void backgroundColorPicker() {
        backgroundColor.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                draw();
            }
        });
    }

    /**
     * Using valueProperty listeners to get
     * Cell color, Grid color and
     * Background color from Users input
     *
     * @author Momcilo Delic - s315282
     */
    @Override
    public void gridColorPicker() {
        gridColor.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                draw();
            }
        });
    }




    // Setters
    @Override
    public void setCellColor(ColorPicker cellColor) {
        this.cellColor = cellColor;
    }
    @Override
    public void setGridColor(ColorPicker gridColor) {
        this.gridColor = gridColor;
    }
    @Override
    public void setBackgroundColor(ColorPicker backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    @Override
    public void setCellSize(double Size) {
        this.cellSize = Size;
    }


    // Getters
    @Override
    public double getCellSize(){
        return cellSize;
    }
    @Override
    public byte[][] getByteArray() {
        byte convertToByte[][] = new byte[board.size()][board.get(0).size()];
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(0).size() ; j++) {
                convertToByte[i][j] = board.get(i).get(j);
            }
        }
        return convertToByte;
    }

    public double getCanvasHeight(){
        return (double) gc.getCanvas().widthProperty().intValue();
    }

    public double getCanvasWidth(){
        return (double) gc.getCanvas().heightProperty().intValue();
    }


}
