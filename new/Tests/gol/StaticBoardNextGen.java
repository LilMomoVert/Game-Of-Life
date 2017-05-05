package gol;

import gol.Model.Board.StaticBoard;
import javafx.scene.canvas.GraphicsContext;
import org.junit.Test;

/**
 * Created by Momcilo Delic on 2/21/2017.
 */
public class StaticBoardNextGen {

    public double                cellSize;
    public StaticBoard staticBoard;
    public GraphicsContext       gc;

    @Test
    public void nextGeneration() throws Exception {
        byte[][] board = {
                { 0, 0, 0, 0, 0 },
                { 0, 0, 1, 0, 0 },
                { 0, 0, 1, 0, 0 },
                { 0, 0, 1, 0, 0 },
                { 0, 0, 0, 0, 0 },
        };

        byte[][] board2 = {
                { 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0 },
                { 0, 1, 1, 0, 0 },
                { 0, 0, 1, 0, 0 },
                { 0, 0, 0, 0, 0 },
        };


        // the current board is 0000000100001000010000000
        // next generation should be 0000000000011100000000000
        // the test is successful

        staticBoard = new StaticBoard(gc, cellSize, 5,5);

        staticBoard.setgameBoard(board);
//        staticBoard.nextGeneration(start, stop);

        org.junit.Assert.assertEquals("0000000000011100000000000", staticBoard.toString());


        staticBoard.setgameBoard(board2);

//       staticBoard.nextGeneration(start, stop);

        // the current board is 00000 00000 01100 00100 00000
        // the next generation board should be 0000000000011000110000000

        // the test is successful
        org.junit.Assert.assertEquals("0000000000011000110000000", staticBoard.toString());
    }

}