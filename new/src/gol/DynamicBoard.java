package gol;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kani Boyka on 4/8/2017.
 */

public class DynamicBoard implements InterfaceBoard {

    @FXML public ColorPicker cellColor;
    @FXML public ColorPicker backgroundColor;
    @FXML public ColorPicker gridColor;


    private ArrayList<ArrayList<Byte>> board;
    private ArrayList<ArrayList<Byte>> randomBoard;
    private byte[][] testBoard;
    int movedistance = 5;
    private double cellSize;
    private GraphicsContext gc;
    private int width;
    private int height;

    /**
     * Constructor
     */
    public DynamicBoard(GraphicsContext gc, double cellSize, int width, int height){
        this.cellSize = cellSize;
        this.gc = gc;
        this.width = width;
        this.height = height;
        newBoard();
    }

    @Override
    public void setLive(int y, int x) {
        board.get(y).set(x, (byte) 1);
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public byte getLive(int x, int y) {
        return board.get(x).get(y);
    }

    @Override
    public void patternUp() {
//        testBoard = new byte[getHeight()][getWidth()];
//
//        for (int x = 0; x < getHeight() - movedistance; x++) {
//            for (int y = 0; y < getWidth(); y++) {
//                if (getLive(y, x).) testBoard[x][y - movedistance] = 1;
//            }
//        }
//        board = testBoard;
//        setgameBoard(testBoard);
//        draw();
    }

    @Override
    public void patternDown() {

    }

    @Override
    public void patternLeft() {

    }

    @Override
    public void patternRight() {

    }

    @Override
    public void setgameBoard(byte[][] newBoard) {
        this.width = newBoard.length;
        this.height = newBoard[0].length;
        this.board = new ArrayList<>();
        for (int i = 0; i < newBoard.length; i++) {
            board.add(new ArrayList<>());
            for (int j = 0; j < newBoard[i].length; j++) {
                board.get(i).add(newBoard[i][j]);
            }
        }
    }

    private void newBoard(){
        board = new ArrayList<>();
        for(int i = 0; i < width; i++){
            board.add(new ArrayList<>());
            for (int j = 0; j < height; j++) {
                board.get(i).add((byte)0);
            }
        }
    }

    public void draw() {
        gc.setFill(backgroundColor.getValue());
        gc.fillRect(0, 0, height *cellSize + 2, width *cellSize + 2);
        gc.setFill(cellColor.getValue());
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).size(); j++) {
                if (board.get(i).get(j) == 1)
                    gc.fillRect(cellSize * j, cellSize * i, cellSize, cellSize);
            }
        }
        drawGrid();
    }

    @Override
    public void nextGeneration() {
        dynamicBoard();
        ArrayList<ArrayList<Byte>> nextBoard = new ArrayList<>();
        for (int i = 0; i < this.width; i++) {
            nextBoard.add(new ArrayList<>());
            for (int j = 0; j < this.height; j++) {
                nextBoard.get(i).add((byte) 0);
            }
        }
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {


                if (countNeighbours(x, y) == 3) {
                    nextBoard.get(x).set(y, (byte) 1);
                } else if (getLive(x, y) == 1 && countNeighbours(x, y) == 2) {
                    nextBoard.get(x).set(y, (byte) 1);
                } else if (countNeighbours(x, y) > 3) {
                    nextBoard.get(x).set(y, (byte) 0);
                } else if (countNeighbours(x, y) < 2) {
                    nextBoard.get(x).set(y, (byte) 0);
                }
            }

        }
        board = nextBoard;
        draw();
    }

    private int countNeighbours(int x, int y) {
        int neighbors = 0;
        if (x > 0) {

            if (board.get(x - 1).get(y) == 1) neighbors++;

            if (y > 0) {
                if (board.get(x - 1).get(y - 1) == 1) neighbors++;
            }

            if (y < this.height - 1) {
                if (board.get(x - 1).get(y + 1) == 1) neighbors++;
            }
        }

        if (y > 0) {
            if (board.get(x).get(y - 1) == 1) {
                neighbors++;
            }
        }

        if (y < this.height - 1) {
            if (board.get(x).get(y + 1) == 1) neighbors++;
        }

        if (x < this.width - 1) {

            if (board.get(x + 1).get(y) == 1) {
                neighbors++;
            }


            if (y > 0) {
                if (board.get(x + 1).get(y - 1) == 1) neighbors++;
            }

            if (y < this.height - 1) {
                if (board.get(x + 1).get(y + 1) == 1) neighbors++;
            }
        }

        return neighbors;

    }


    @Override
    public void ClearBtn(){
        board.clear();
        for (int x = 0; x < width; x++) {
            board.add(new ArrayList<>());
            for(int y = 0; y < height; y++){
                board.get(x).add((byte) 0);
            }
        }
    }

    public void drawGrid(){
        for(double i=0; i< board.size() + 1; i++){
            gc.setStroke(gridColor.getValue());
            gc.strokeLine(0, i*cellSize, board.get(0).size()*cellSize, i*cellSize);
        }

        for(double j=0; j < board.get(0).size() + 1; j++){
            gc.setStroke(gridColor.getValue());
            gc.strokeLine(j*cellSize, 0, j*cellSize, board.size()*cellSize);
        }
    }

    private void dynamicBoard() {
        for (int y = 0; y < this.width; y++) {
            if (board.get(y).get(0) == 1) {
                this.height++;
                for (int i = 0; i < this.width; i++) {
                    board.get(i).add(0, (byte) 0);
                }
            }
            if (board.get(y).get(height - 1) == 1) {
                this.height++;
                for (int i = 0; i < this.width; i++) {
                    board.get(i).add((byte) 0);
                }
            }
        }

        for (int x = 0; x < this.height; x++) {
            if (board.get(0).get(x) == 1) {
                this.width++;
                board.add(0, new ArrayList<>());
                for (int i = 0; i < this.height; i++) {
                    board.get(0).add((byte) 0);
                }
            }
            if (board.get(width - 1).get(x) == 1) {
                this.width++;
                board.add(new ArrayList<>());
                for (int i = 0; i < this.height; i++) {
                    board.get(width - 1).add((byte) 0);
                }
            }
        }
    }

    public void Randomness(){
        Random random = new Random();
        for (int i = 0; i < width ; i++) {
            for (int j = 0; j < height; j++) {
                board.get(i).set(j,(byte) random.nextInt(2));
            }
        }

    }


    /**
     * Using valueProperty listeners to get
     * Cell color, Grid color and
     * Background color from Users input
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

    @Override
    public void backgroundColorPicker() {
        backgroundColor.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                draw();
            }
        });
    }

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

}
