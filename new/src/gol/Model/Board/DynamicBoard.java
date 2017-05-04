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
    private int                             width, height;
    private int                             movedistance = 1;
    private double                          cellHeight, cellWidth, cellSize;
    private ArrayList<ArrayList<Byte>>      board, patternExtend;
    private GraphicsContext                 gc;
    public boolean                          circle;
    public boolean                          dynamicSize = true;



    /**
     * Constructor
     */
    public DynamicBoard(GraphicsContext gc, double cellSize, int height, int width){
        this.cellSize = cellSize;
        this.gc = gc;
        this.width = height;
        this.height = width;
        newBoard();
    }

    @Override
    public void setLive(int x, int y, byte state) {
        board.get(x).set(y, state);
    }

    @Override
    public int getHeight() {
        return board.get(0).size();
    }

    @Override
    public int getWidth() {
        return board.size();
    }

    @Override
    public byte getLive(int x, int y) {
        return board.get(x).get(y);
    }

    public void setCircle(boolean circle){
        this.circle = circle;
    }

    public void setDynamicSize(boolean dynamicSize){
        this.dynamicSize = dynamicSize;
    }

    public double getCanvasHeight(){
        return (double) gc.getCanvas().widthProperty().intValue();
    }

    public double getCanvasWidth(){
        return (double) gc.getCanvas().heightProperty().intValue();
    }

    public void draw() {
        gc.clearRect(0, 0, getCanvasHeight(), getCanvasWidth());
        gc.setFill(backgroundColor.getValue());
        gc.fillRect(0, 0, getCanvasHeight(), getCanvasWidth());
        gc.setFill(cellColor.getValue());
        if(dynamicSize) {
            cellHeight = getCanvasHeight() / getHeight();
            cellWidth = getCanvasWidth() / getWidth();
            if (cellWidth < cellHeight) {
                cellSize = cellWidth;
            } else {
                cellSize = cellHeight;
            }
        }
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (getLive(i, j) == 1) {
                    if(circle == false) {
                        gc.fillRect(cellSize * j, cellSize * i, cellSize, cellSize);
                    } else {
                        gc.fillOval(cellSize * j, cellSize * i, cellSize, cellSize);
                    }
                }
            }
        }
        drawGrid();
    }


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
            for (int y = 0; y < board.get(x).size() - 1; y++) {
                if (getLive(x, y) == 1)
                    patternExtend.get(x - movedistance).set(y, (byte) 1);
            }
        }
        board = patternExtend;
        draw();
    }

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
        for (int x = 0; x < board.size() - 1; x++) {
            for (int y = 0; y < board.get(x).size(); y++) {
                if (getLive(x, y) == 1)
                    patternExtend.get((x + movedistance)).set(y, (byte) 1);
            }
        }
        board = patternExtend;
        draw();
    }

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
            for (int y = 0; y < board.get(x).size() - 1; y++) {
                if (getLive(x, y) == 1)
                    patternExtend.get(x).set(y + movedistance, (byte) 1);
            }
        }
        board = patternExtend;
        draw();
    }

    @Override
    public void setgameBoard(byte[][] newGameBoard) {
        this.width = newGameBoard.length;
        this.height = newGameBoard[0].length;
        this.board = new ArrayList<>();
        for (int i = 0; i < newGameBoard.length; i++) {
            board.add(new ArrayList<>());
            for (int j = 0; j < newGameBoard[i].length; j++) {
                board.get(i).add(newGameBoard[i][j]);
            }
        }
    }

    public void setgameBoard(ArrayList<ArrayList<Byte>> newBoard) {
        this.width = newBoard.size();
        this.height = newBoard.get(0).size();
        this.board = new ArrayList<>();
        for (int i = 0; i < newBoard.size(); i++) {
            board.add(new ArrayList<>());
            for (int j = 0; j < newBoard.get(i).size(); j++) {
                board.get(i).add(newBoard.get(i).get(j));
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

    @Override
    public void nextGeneration(int start, int stop) {
        addLeft();
        addRight();
        addTop();
        addDown();

        ArrayList<ArrayList<Byte>> nextBoard = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            nextBoard.add(new ArrayList<>());
            for (int j = 0; j < height; j++) {
                nextBoard.get(i).add((byte) 0);
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {


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

    public void fastPopulateRule() {
        addLeft();
        addRight();
        addTop();
        addDown();

        ArrayList<ArrayList<Byte>> nextBoard = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            nextBoard.add(new ArrayList<>());
            for (int j = 0; j < height; j++) {
                nextBoard.get(i).add((byte) 0);
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                if (board.get(x).get(y)==1){
                    nextBoard.get(x).set(y,(byte)1);
                }
                else if (countNeighbours(x,y) == 3){
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

    @Override
    public void ImprovizedRule() {
        addLeft();
        addRight();
        addTop();
        addDown();
        ArrayList<ArrayList<Byte>> nextBoard = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            nextBoard.add(new ArrayList<>());
            for (int j = 0; j < height; j++) {
                nextBoard.get(i).add((byte) 0);
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

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

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < board.size(); i++){
            for(int j = 0; j < board.get(i).size(); j++){
                stringBuffer.append(board.get(i).get(j));
            }
        }
        return stringBuffer.toString();
    }

    private int countNeighbours(int x, int y) {
        int cNeighbour = 0;
        if (x > 0) {
            if (getLive(x - 1, y) == 1)
                cNeighbour++;

            if (y > 0) {
                if (getLive(x - 1,y - 1) == 1)
                    cNeighbour++;
            }

            if (y < height - 1) {
                if (getLive(x - 1,y + 1) == 1)
                    cNeighbour++;
            }
        }
        if (y > 0) {
            if (getLive(x,y - 1) == 1) {
                cNeighbour++;
            }
        }
        if (y < height - 1) {
            if (getLive(x,y + 1) == 1)
                cNeighbour++;
        }
        if (x < width - 1) {
            if (getLive(x + 1, y) == 1) {
                cNeighbour++;
            }
            if (y > 0) {
                if (getLive(x + 1,y - 1) == 1)
                    cNeighbour++;
            }
            if (y < height - 1) {
                if (getLive(x + 1,y + 1) == 1)
                    cNeighbour++;
            }
        }
        return cNeighbour;
    }


    @Override
    public void ClearButton(){
        board.clear();
        for (int x = 0; x < width; x++) {
            board.add(new ArrayList<>());
            for(int y = 0; y < height; y++){
                board.get(x).add((byte) 0);
            }
        }
        setgameBoard(new byte[45][60]);
    }

    private void drawGrid(){
        if (cellSize > 2.5 ) {
            for (double i = 0; i < board.size() + 1; i++) {
                gc.setStroke(gridColor.getValue());
                gc.strokeLine(0, i * cellSize, board.get(0).size() * cellSize, i * cellSize);
            }

            for (double j = 0; j < board.get(0).size() + 1; j++) {
                gc.setStroke(gridColor.getValue());
                gc.strokeLine(j * cellSize, 0, j * cellSize, board.size() * cellSize);
            }
        }
    }


    public void addLeft() {
        for (int y = 0; y < width; y++) {
            if (board.get(y).get(0) == 1) {
                height++;
                for (int i = 0; i < width; i++) {
                    board.get(i).add(0, (byte) 0);
                }
            }
        }
    }

    public void addTop() {
        for (int x = 0; x < height; x++) {
            if (board.get(0).get(x) == 1) {
                width++;
                ArrayList<Byte> temp = new ArrayList<Byte>();
                for (int i = 0; i < height; i++) {
                    temp.add((byte) 0);
                }
                board.add(0, temp);
            }
        }
    }

    public void addRight(){
        for (int y = 0; y < width; y++) {
            if (board.get(y).get(height - 2) == 1) {
                height++;
                for (int i = 0; i < width; i++) {
                    board.get(i).add((byte) 0);
                }
            }
        }
    }

    public void addDown(){
        int inc = 1;

        for (int x = 0; x < height; x++) {
            if (board.get(width - 2).get(x) == 1) {
                width++;
                board.add(new ArrayList<>());
                for (int i = 0; i < height; i++) {
                    board.get(width - inc).add((byte) 0);
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


}
