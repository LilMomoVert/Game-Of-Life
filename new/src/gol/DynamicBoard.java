package gol;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

/**
 * Created by Kani Boyka on 4/8/2017.
 */

public class DynamicBoard {

    private ArrayList<ArrayList<Byte>> board;
    private int row;
    private int column;

    /**
     * Constructor
     */

    public DynamicBoard(int row, int column){
        this.row = row;
        this.column = column;
        newBoard();
    }

    private void newBoard(){
        board = new ArrayList<>();
        for(int i = 0; i < row; i++){
            board.add(new ArrayList<>());
            for (int j = 0; j < column; j++) {
                board.get(i).add((byte)0);
            }
        }
    }

    public byte getCellState(int row, int col) {

        return board.get(row).get(col);
    }

    public void setCellState(int x, int y, byte state) {
        board.get(x).set(y, state);
    }

    public void setBoard(byte[][] newBoard) {
        this.row = newBoard.length;
        this.column = newBoard[0].length;
        this.board = new ArrayList<>();
        for (int i = 0; i < newBoard.length; i++) {
            board.add(new ArrayList<>());
            for (int j = 0; j < newBoard[i].length; j++) {
                board.get(i).add(newBoard[i][j]);
            }
        }

    }

    private int checkNeighbors(int row, int column) {
        int neighbors = 0;
        if (row > 0) {

            if (board.get(row - 1).get(column) == 1) neighbors++;

            if (column > 0) {
                if (board.get(row - 1).get(column - 1) == 1) neighbors++;
            }

            if (column < this.column - 1) {
                if (board.get(row - 1).get(column + 1) == 1) neighbors++;
            }
        }

        if (column > 0) {
            if (board.get(row).get(column - 1) == 1) {
                neighbors++;
            }
        }

        if (column < this.column - 1) {
            if (board.get(row).get(column + 1) == 1) neighbors++;
        }

        if (row < this.row - 1) {

            if (board.get(row + 1).get(column) == 1) {
                neighbors++;
            }


            if (column > 0) {
                if (board.get(row + 1).get(column - 1) == 1) neighbors++;
            }

            if (column < this.column - 1) {
                if (board.get(row + 1).get(column + 1) == 1) neighbors++;
            }
        }

        return neighbors;

    }

    private void expandBoard() {
        for (int y = 0; y < this.row; y++) {
            if (board.get(y).get(0) == 1) {
                this.column++;
                for (int i = 0; i < this.row; i++) {
                    board.get(i).add(0, (byte) 0);
                }
            }
            if (board.get(y).get(column - 1) == 1) {
                this.column++;
                for (int i = 0; i < this.row; i++) {
                    board.get(i).add((byte) 0);
                }
            }
        }

        for (int x = 0; x < this.column; x++) {
            if (board.get(0).get(x) == 1) {
                this.row++;
                board.add(0, new ArrayList<>());
                for (int i = 0; i < this.column; i++) {
                    board.get(0).add((byte) 0);
                }
            }
            if (board.get(row - 1).get(x) == 1) {
                this.row++;
                board.add(new ArrayList<>());
                for (int i = 0; i < this.column; i++) {
                    board.get(row - 1).add((byte) 0);
                }
            }
        }
    }

    public void nextGen() {
        expandBoard();

        ArrayList<ArrayList<Byte>> nextGenBoard = new ArrayList<>();
        for (int i = 0; i < this.row; i++) {
            nextGenBoard.add(new ArrayList<>());
            for (int j = 0; j < this.column; j++) {
                nextGenBoard.get(i).add((byte) 0);
            }
        }
        for (int row = 0; row < this.row; row++) {
            for (int column = 0; column < this.column; column++) {


                if (checkNeighbors(row, column) == 3) {
                    nextGenBoard.get(row).set(column, (byte) 1);
                } else if (getCellState(row, column) == 1 && checkNeighbors(row, column) == 2) {
                    nextGenBoard.get(row).set(column, (byte) 1);
                } else if (checkNeighbors(row, column) > 3) {
                    nextGenBoard.get(row).set(column, (byte) 0);
                } else if (checkNeighbors(row, column) < 2) {
                    nextGenBoard.get(row).set(column, (byte) 0);
                }
            }

        }
        board = nextGenBoard;
    }


























    //Setters
    public void setRow(int row) {
        this.row = row;
    }
    public void setColumn(int column) {
        this.column = column;
    }

    //Getters
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
