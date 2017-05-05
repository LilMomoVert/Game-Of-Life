package gol.Model.Board;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;

import java.util.Random;

/**
 * Created by Momcilo Delic on 3/19/2017.
 *
 * The class implements an Interface
 * This will make it easier for me to switch between the Static Board
 * and Dynamic Board methods in my controller.
 */
public class StaticBoard implements InterfaceBoard {

    //=========================================================================//
    //                             @FXML                                       //
    //=========================================================================//
    @FXML private ColorPicker            cellColor;
    @FXML private ColorPicker            backgroundColor;
    @FXML private ColorPicker            gridColor;

    //=========================================================================//
    //                             Variabler                                   //
    //=========================================================================//
    private int                         width;
    private int                         height;
    private int                         movedistance = 1;
    private double                      cellSize;
    private byte[][]                    board;
    private byte[][]                    randomBoard;
    private GraphicsContext             gc;
    protected boolean                   dynamicSize = false;
    protected boolean                   circle = true;



    /**
     * Making a constructor for StaticBoard class with parameters
     * gc, setCellSize, height and the Width
     * @author Momcilo Delic - s315282
     */
    public StaticBoard(GraphicsContext gc, double cellSize, int height, int width) {
        this.cellSize = cellSize;
        this.gc = gc;
        this.height = height;
        this.width = width;
        this.board = new byte[height][width];
    }

    /**
     * Draw method, gets the value from cell Colorpicker
     * and background Colorpicker
     *
     * @author Momcilo Delic - s315282
     */
    public void draw() {
        gc.setFill(backgroundColor.getValue());
        gc.fillRect(0, 0, width *cellSize, height *cellSize);
        gc.setFill(cellColor.getValue());
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (getLive(i,j) == 1)
                    if(circle == true) {
                        gc.fillRect(cellSize * j, cellSize * i, cellSize, cellSize);
                    } else {
                        gc.fillOval(cellSize * j, cellSize * i, cellSize, cellSize);
                    }
                }
            }
        drawGrid();
    }

    /**
     * Next generation, counts neighbours and sets
     * next generation. If there is less than two neighbours
     * the cell dies. Cells with two or three neighbours continues
     * to live. Cells with more than three neighbours die. If a dead
     * cell is surrounded with 3 alive cells, it becomes alive.
     *
     * @author Momcilo Delic - s315282
     * @param start
     * @param stop
     */
    public void nextGeneration(int start, int stop){
        byte[][] nextBoard = new byte[board.length][board[0].length];

        for (int x = 1; x < board.length; x++) {
            for (int y = 1; y < board[0].length; y++) {

                if ((getLive(x,y) == 1)&&(countNeighbours(x,y)  < 2)){
                    nextBoard[x][y] = 0;
                }
                else if ((getLive(x,y) == 1)&&(countNeighbours(x,y)  > 3)){
                    nextBoard[x][y] = 0;
                }
                else if ((getLive(x,y) == 0)&&(countNeighbours(x,y)  == 3)){
                    nextBoard[x][y] = 1;
                }
                else {
                    nextBoard[x][y] = getLive(x,y);
                }
            }
        }
        board = nextBoard;
        draw(); // If nextGeneration is being tested, comment out Draw method
    }

    @Override
    public void ImprovizedRule() {
        byte[][] nextBoard = new byte[board.length][board[0].length];

        for (int x = 1; x < board.length; x++) {
            for (int y = 1; y < board[0].length; y++) {

                int neighbors = countNeighbours(x,y);

                if ((neighbors == 1)){
                    nextBoard[x][y] = 0;
                }
                else if ((neighbors == 2)){
                    nextBoard[x][y] = 1;
                }
                else if ((neighbors == 3)){
                    nextBoard[x][y] = 1;
                }
            }
        }
        board = nextBoard;
        draw();
    }

    @Override
    public void fastPopulateRule() {
        byte[][] nextBoard = new byte[board.length][board[0].length];

        for (int x = 1; x < board.length; x++) {
            for (int y = 1; y < board[0].length; y++) {

                if ((getLive(x,y) == 1)&&(countNeighbours(x,y) < 2)){
                    nextBoard[x][y] = 1;
                }
                else if ((getLive(x,y) == 1)&&(countNeighbours(x,y)  > 3)){
                    nextBoard[x][y] = 1;
                }
                else if ((getLive(x,y) == 0)&&(countNeighbours(x,y)  == 3)){
                    nextBoard[x][y] = 1;
                }
                else {
                    nextBoard[x][y] = getLive(x,y);
                }
            }
        }
        board = nextBoard;
        draw();
    }

    private int countNeighbours(int x, int y) {
            int cNeighbours = 0;

            if (x != getWidth() - 1)
                if (getLive(x + 1, y) == 1)
                    cNeighbours++;

            if (x != getWidth() - 1 && y != getHeight() - 1)
                if (getLive(x + 1, y + 1) == 1)
                    cNeighbours++;

            if (y != getHeight() - 1)
                if (getLive(x, y + 1) == 1)
                    cNeighbours++;

            if (x != 0 && y != getHeight() - 1)
                if (getLive(x - 1, y + 1)== 1)
                    cNeighbours++;

            if (x != 0)
                if (getLive(x - 1, y) == 1)
                    cNeighbours++;

            if (x != 0 && y != 0)
                if (getLive(x - 1,y - 1) == 1)
                    cNeighbours++;

            if (y != 0)
                if (getLive(x, y - 1) == 1)
                    cNeighbours++;

            if (x != getWidth() - 1 && y != 0)
                if (getLive(x + 1, y -1) == 1)
                    cNeighbours++;

            return cNeighbours;
        }

    /**
     * Clears the board, creates a new one
     * @author Momcilo Delic - s315282
     */
    public void ClearButton(){
        board = new byte[height][width];
        randomBoard = new byte[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                randomBoard[i][j] = (1);
            }
        }
    }

    @Override
    public void setLive(int y, int x, byte state){
        board[y][x] = state;
    }

    @Override
    public byte getLive(int x, int y) {
        return board[x][y];
    }

    /**
     * Accessing the Random Java class
     * Making new byte board (randomBoard)
     * Setting the randomGenerator to 2
     *
     * @author Momcilo Delic - s315282
     */
    public void Randomness(){
        Random randomGenerator = new Random();
        byte[][] randomBoard = new byte[board.length][board[0].length];
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                randomBoard[y][x] = (byte)randomGenerator.nextInt(2);
            }
            board = randomBoard;
        }
    }

    /**
     * Colorpickers for cellColor, Background color and the Gridcolor
     * @author Momcilo Delic - s315282
     */
    public void cellColorPicker(){
        cellColor.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                draw();
            }
        });
    }

    public void backgroundColorPicker(){
        backgroundColor.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                draw();
            }
        });
    }

    public void gridColorPicker(){
        gridColor.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                draw();
            }
        });
    }

    /**
     * Draw grid method. The grid turns of if the size of the board
     * is too big. (Because the grid looks really bad and its basically a background)
     *
     * @author Momcilo Delic - s315282
     */
    private void drawGrid(){
        if(cellSize > 2.5) {
            for (double i = 0; i < board.length + 1; i++) {
                gc.setStroke(gridColor.getValue());
                gc.strokeLine(0, i * cellSize, board[0].length * cellSize, i * cellSize);
            }

            for (double j = 0; j < board[0].length + 1; j++) {
                gc.setStroke(gridColor.getValue());
                gc.strokeLine(j * cellSize, 0, j * cellSize, board.length * cellSize);
            }
        }
    }

    /**
     * Make a new byte board for when the pattern is moved up
     * setting the old board = patternUp board
     *
     * @author Momcilo Delic - s315282
     */
    public void patternUp(){
            byte[][] patternUp = new byte[getWidth()][getHeight()];

            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    if (getLive(x, y) == 1) patternUp[x - movedistance][y] = 1;
                }
            }
            board = patternUp;
    }

    public void patternDown(){
            byte[][] patternDown = new byte[getWidth()][getHeight()];

            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    if (getLive(x, y) == 1) patternDown[x + movedistance][y] = 1;
                }
            }
            board = patternDown;
            setgameBoard(patternDown);
            draw();

    }

    public void patternLeft(){
            byte[][] patternLeft = new byte[getWidth()][getHeight()];

            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    if (getLive(x, y) == 1) patternLeft[x][y - movedistance] = 1;
                }
            }
            board = patternLeft;
            setgameBoard(patternLeft);
            draw();

    }

    public void patternRight(){
            byte[][] patternRight = new byte[getWidth()][getHeight()];

            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++){
                    if (getLive(x, y) == 1) patternRight[x][y + movedistance] = 1;
                }
            }
            board = patternRight;
            setgameBoard(patternRight);
            draw();

    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                stringBuilder.append(getLive(i,j));
            }
        }
        return stringBuilder.toString();
    }

    // Setters
    @Override
    public void setCellColor(ColorPicker cellColor){
        this.cellColor = cellColor;
    }
    @Override
    public void setGridColor(ColorPicker gridColor){
        this.gridColor = gridColor;
    }
    @Override
    public void setBackgroundColor(ColorPicker backgroundColor){
        this.backgroundColor = backgroundColor;
    }
    @Override
    public void setCellSize(double Size){
        this.cellSize = Size;
    }
    @Override
    public void setgameBoard(byte[][] newGameBoard) {
        this.board = newGameBoard;
    }
    @Override
    public void setCircle(boolean circle) {
        this.circle = circle;
    }
    @Override
    public void setDynamicSize(boolean dynamicSize) {
        this.dynamicSize = dynamicSize;
    }



    // Getters
    public byte[][] getBoard(){
        return board;
    }
    @Override
    public double getCellSize() {
        return cellSize;
    }
    @Override
    public byte[][] getByteArray() {
        return board;
    }
    @Override
    public int getWidth() {
        return board.length;
    }
    @Override
    public int getHeight() {
        return board[0].length;
    }

}


