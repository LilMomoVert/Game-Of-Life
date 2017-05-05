package gol;

import gol.Model.Board.DynamicBoard;
import javafx.scene.canvas.GraphicsContext;
import org.junit.Test;

/**
 * Created by Momcilo Delic on 3/15/2017.
 */
public class DynamicBoardTest {
    public double       cellSize;
    public DynamicBoard dynamicBoard;
    public GraphicsContext gc;

    @Test
    public void nextGeneration() throws Exception {
        byte[][] board = {
                { 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1 },
                { 0, 0, 0, 0, 1 },
                { 0, 0, 0, 0, 1 },
                { 0, 0, 0, 0, 0 },
        };


        // the current board is             0000000001000010000100000
        // next generation should be        000000000000000111000000000000 because the board expands

        dynamicBoard = new DynamicBoard(gc, cellSize, 5,5);

        dynamicBoard.setgameBoard(board);
//        dynamicBoard.nextGeneration(start, stop);

        org.junit.Assert.assertEquals("000000000000000111000000000000", dynamicBoard.toString());
    }


}