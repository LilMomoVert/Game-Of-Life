import gol.Board;
import gol.Controller;
import javafx.scene.canvas.GraphicsContext;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Kani Boyka on 4/3/2017.
 */
public class ControllerTest {

    private GraphicsContext gc;
    Board gameBoard = new Board(gc, 5, 100, 100);

    @Test
    public void nextGen() throws Exception{
        byte[][] testBoard4 =  {
                { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
                { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
        };

        gameBoard.setgameBoard(testBoard4);
        nextGen();

        String expected = "0100000000001100000001100000000000000000000000000000000000000000000000000000000000000000000000000000";
        String actual = gameBoard.toString();
        org.junit.Assert.assertEquals(actual,expected);

        // Console
        System.out.println("Expected: " +expected);
        System.out.println("Actual:   " +actual);
    }
}