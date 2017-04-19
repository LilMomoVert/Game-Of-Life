package gol;

import javafx.scene.canvas.GraphicsContext;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Kani Boyka on 4/4/2017.
 */
public class nextGenTest {

    private GraphicsContext gc;
    Board gameBoard = new Board(gc, 5, 100,100);

    @Test
    public void nextGen() throws Exception {
        byte[][] testBoard3 = {
                { 0, 1, 0, 0 },
                { 0, 0, 1, 0 },
                { 1, 1, 1, 0 },
                { 0, 0, 0, 0 }
        };


        gameBoard.setgameBoard(testBoard3);
        nextGen();

        String expected = "0100001101100000";
        String actual = gameBoard.toString();
        org.junit.Assert.assertEquals(actual,expected);

        System.out.println("Expected: " +expected);
        System.out.println("Actual:   " +actual);
    }

}