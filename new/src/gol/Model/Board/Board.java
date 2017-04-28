package gol.Model.Board;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;

import java.util.Random;

/**
 * Created by Momcilo Delic on 3/19/2017.
 */
public class Board implements InterfaceBoard {

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
    private int                         movedistance = 5;
    private double                      cellSize;
    private byte[][]                    board;
    private byte[][]                    randomBoard;
    private GraphicsContext             gc;



    /**
     * Making a constructor for Board class with parameters
     * gc, setCellSize, height and the Width
     * @author Momcilo Delic - s315282
     */
    public Board(GraphicsContext gc, double cellSize, int height, int width) {
        this.cellSize = cellSize;
        this.gc = gc;
        this.height = height;
        this.width = width;
        this.board = new byte[height][width];
    }

    public void setLive(int y, int x, byte state){
        board[y][x] = state;
    }

    @Override
    public int getHeight() {
        return board.length;
    }

    @Override
    public int getWidth() {
        return board[0].length;
    }

    @Override
    public byte getLive(int x, int y) {
        return board[x][y];
    }

    /**
     * @param board For loop board lenght (Changed from Width and Height)
     *              the reason is because i want the loop to go through the
     *              board lenght instead of the whole height and width
     * @author Momcilo Delic - s315282
     */
    public void setgameBoard(byte[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }

    /**
     * Draw method, gets the value from cell Colorpicker
     * and background Colorpicker
     *
     * @author Momcilo Delic - s315282
     */
    public void draw() {
        gc.setFill(backgroundColor.getValue());
        gc.fillRect(0, 0, width *cellSize + 2, height *cellSize + 2);
        gc.setFill(cellColor.getValue());
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 1)
                    gc.fillOval(cellSize * j, cellSize * i, cellSize, cellSize);
            }
        }
        drawGrid();
    }

    /**
     * Next generation, counts neighbours and sets
     * next generation. If there is less than two neighbours
     * the cell dies. LifeDecoder with two or three neighbours continues
     * to live. LifeDecoder with more than three neighbours die. If a dead
     * cell is surrounded with 3 alive cells, it becomes alive.
     *
     * @author Momcilo Delic - s315282
     */
    public void nextGeneration(){
        byte[][] nextBoard = new byte[board.length][board[0].length];
        for (int x = 1; x < board.length; x++) {
            for (int y = 1; y < board[0].length; y++) {

                int neighbors = countNeighbours(x,y);

                if ((getLive(x,y) == 1)&&(neighbors < 2)){
                    nextBoard[x][y] = 0;
                }
                else if ((getLive(x,y) == 1)&&(neighbors > 3)){
                    nextBoard[x][y] = 0;
                }
                else if ((getLive(x,y) == 0)&&(neighbors == 3)){
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
        int livingNeighbours = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (j == 0 && i == 0) {
                    continue;
                }
                try {
                    if (board[x + i][y + j] == 1) {
                        livingNeighbours++;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }
        return livingNeighbours;
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

    /**
     * Accessing the Rrandom Java class
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
        if(cellSize > 2) {
            for (double i = 0; i < board.length + 1; i++) {
                gc.setStroke(gridColor.getValue());
                gc.strokeLine(0, i * cellSize, width * cellSize, i * cellSize);
            }

            for (double j = 0; j < board[0].length + 1; j++) {
                gc.setStroke(gridColor.getValue());
                gc.strokeLine(j * cellSize, 0, j * cellSize, height * cellSize);
            }
        }
    }

    public void patternUp(){
            byte[][] testBoard = new byte[getHeight()][getWidth()];

            for (int x = 0; x < getHeight(); x++) {
                for (int y = 0; y < getWidth(); y++) {
                    if (getLive(x, y) == 1) testBoard[x - movedistance][y] = 1;
                }
            }
            board = testBoard;
            setgameBoard(testBoard);
            draw();
    }

    public void patternDown(){
            byte[][] testBoard = new byte[getHeight()][getWidth()];

            for (int x = 0; x < getHeight(); x++) {
                for (int y = 0; y < getWidth(); y++) {
                    if (getLive(x, y) == 1) testBoard[x + movedistance][y] = 1;
                }
            }
            board = testBoard;
            setgameBoard(testBoard);
            draw();

    }

    public void patternLeft(){
            byte[][] testBoard = new byte[getHeight()][getWidth()];

            for (int x = 0; x < getHeight(); x++) {
                for (int y = 0; y < getWidth(); y++) {
                    if (getLive(x, y) == 1) testBoard[x][y - movedistance] = 1;
                }
            }
            board = testBoard;
            setgameBoard(testBoard);
            draw();

    }

    public void patternRight(){
            byte[][] testBoard = new byte[getHeight()][getWidth()];

            for (int x = 0; x < getHeight(); x++) {
                for (int y = 0; y < getWidth(); y++){
                    if (getLive(x, y) == 1) testBoard[x][y + movedistance] = 1;
                }
            }
            board = testBoard;
            setgameBoard(testBoard);
            draw();

    }

    // Setters
    public void setCellColor(ColorPicker cellColor){
        this.cellColor = cellColor;
    }
    public void setGridColor(ColorPicker gridColor){
        this.gridColor = gridColor;
    }
    public void setBackgroundColor(ColorPicker backgroundColor){
        this.backgroundColor = backgroundColor;
    }
    public void setCellSize(double Size){
        this.cellSize = Size;
    }

    // Getters
    public double getCellSize(){
        return this.cellSize;
    }
    public byte[][] getBoard(){
        return board;
    }
}


