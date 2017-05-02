package gol;

import gol.Model.Board.Board;
import javafx.scene.canvas.GraphicsContext;
import org.junit.Test;

/**
 * Created by Kani Boyka on 4/29/2017.
 */
public class StaticBoardNextGen {

    public double                cellSize;
    public Board                 staticBoard;
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


        // the current board is 0000000100001000010000000
        // next generation should be 0000000000011100000000000

        staticBoard = new Board(gc, cellSize, 5,5);

        staticBoard.setgameBoard(board);
        staticBoard.nextGeneration();

        org.junit.Assert.assertEquals("0000000000011100000000000", staticBoard.toString());
    }

}