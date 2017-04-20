package gol;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.paint.Color.*;

/**
 * Created by Momcilo Delic on 2/10/2017.
 */

public class Controller implements Initializable {
    //=========================================================================//
    //                             @FXML                                       //
    //=========================================================================//
    @FXML public Button oneStep;
    @FXML public Button playStop;
    @FXML public Slider speedSlider;
    @FXML public Slider sizeSlider;
    @FXML public ColorPicker cellColor;
    @FXML public ColorPicker backgroundColor;
    @FXML public ColorPicker gridColor;
    @FXML public Label gridOff;
    @FXML public CheckBox setGrid;
    //=========================================================================//
    //                             Variables                                   //
    //=========================================================================//
    public Canvas theCanvas;
    public GraphicsContext gc;
    public double cellSize;
    public byte[][] board;
    public byte[][] randomBoard;
    public int theHeight;
    public int theWidth;
    public int X;
    public int Y;
    private boolean nonDynamic;

    //=========================================================================//
    //                             References                                  //
    //=========================================================================//
    Information info;
    FileHandler handler;
    Board gameBoard;
    DynamicBoard dynboard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Setting Gcontext, cellSize, Width and Height
        // @author Momcilo Delic - s315282
        nonDynamic = true;
        cellSize = 10;
        gc = theCanvas.getGraphicsContext2D();
        setGrid.setSelected(true);

        //=========================================================================//
        //                             Objects                                     //
        //=========================================================================//
        info = new Information();
        handler = new FileHandler();
        gameBoard = new Board(gc, cellSize, 2000, 2000 );
        //heeei

        //=========================================================================//
        //                               Colorpickers                              //
        //=========================================================================//

        // Colorpickere med startfarge
        // @Author Momcilo Delic - s315282
        backgroundColor.setValue(BLACK);
        gridColor.setValue(RED);
        cellColor.setValue(WHITE);

        gameBoard.setGridOff(gridOff);
        gameBoard.setCellColor(cellColor);
        gameBoard.setGridColor(gridColor);
        gameBoard.setBackgroundColor(backgroundColor);
        gameBoard.setGrid(setGrid);

        // Colorpickere med Listener so you can change the color
        // instantly instead of waiting for the next generation for it to apply.
        // Color Listen methods are in Board class
        // @Author Momcilo Delic - s315282

        gameBoard.cellColorPicker();
        gameBoard.backgroundColorPicker();
        gameBoard.gridColorPicker();
        sizeSlider();
        speedSlider();

        // Set GraphicsContexts
        gc = theCanvas.getGraphicsContext2D();
        gc.fillRect(0, 0, theCanvas.getWidth(), theCanvas.getHeight());

        //Drawing the Cells and the Grid
        gameBoard.draw();
        gameBoard.setGrid();
    }


    // Size listener
    public void sizeSlider(){
        sizeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, theCanvas.getWidth(), theCanvas.getHeight());
                gameBoard.cellSize(sizeSlider.getValue());
                sizeSlider.setMin(0.3); //testing
                sizeSlider.setMax(100);
                gameBoard.draw();
                gameBoard.setGrid();
            }
        });
    }

    // Speed listener
    public void speedSlider(){
        speedSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                setSpeed(speedSlider.getValue());
                speedSlider.setMin(1);
                speedSlider.setMax(100);
                //System.out.println(speedSlider.getValue());
            }
        });
    }



    public void test(){
        byte[][] testBoard4 =  {
                { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 1, 1, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1 },
                { 0, 0, 0, 0, 0, 0, 0, 1, 0, 1 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }
        };
        gameBoard.setgameBoard(testBoard4);
        gameBoard.draw();
        gameBoard.setGrid();
        System.out.println("hei");
    }


    // Putting Timeline outside of initialize
    // Timeline jumps to start to the duration ZERO
    // tl plays with Duration 200 milliseconds
    // @Author Momcilo Delic - s315282
    public Timeline tl;{
        tl = new Timeline(new KeyFrame(Duration.millis(200), event -> {;
            nextGen();
            tl.stop();
            tl.setRate(Math.abs(tl.getRate()));
            tl.jumpTo(Duration.ZERO);
            tl.play();
            //System.out.println(tl.getRate());
        }));
    }



    // Information about the game and author
    // @Author Momcilo Delic - s315282
    public void about(){
        info.aboutGOL();
    }

    public void madeBy(){
        info.authorInfo();
    }

    public void setSpeed(double speed){
        tl.setRate(speed);
    }


    /**
     *  @author Momcilo Delic
     */
    public void MouseDraw(MouseEvent event) {

    try {
        int x = (int) (event.getX() / gameBoard.cellSize);
        int y = (int) (event.getY() / gameBoard.cellSize);

        if (x != X || y != X) {

            if (gameBoard.board[y][x] == 0) {
                gameBoard.board[y][x] = 1;
            }
        }
        X = x;
        Y = y;
        gameBoard.draw();
        gameBoard.setGrid();
    } catch (ArrayIndexOutOfBoundsException AOB) {
        info.AOBCanvas();
    }
    }

    // Set large board (The grid will turn of since the board is too big
    // and the grid isn't usefull anymore)
    // @Author Momcilo Delic - s315282
    public void largeBoard(){
        gameBoard.cellSize = 2;
        gameBoard.draw();
        gameBoard.setGrid();
    }
    //Set board, normal size
    public void mediumBoard(){
        gameBoard.cellSize = 5;
        gameBoard.draw();
        gameBoard.setGrid();
    }
    //Set board, small size
    public void smallBoard(){
        gameBoard.cellSize = 20;
        gameBoard.draw();
        gameBoard.setGrid();
    }


    /**
     *
     */
    public void nextGen(){
        gameBoard.nextGeneration();
    }

    // Random funskjon for å generere celler
    // @Author Momcilo Delic - s315282
    public void randomGame(){
        gameBoard.Randomness();
        gameBoard.draw();
        gameBoard.setGrid();
    }


    public void fileChooser() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open your file");


        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("RLE", "*.rle"),
                new FileChooser.ExtensionFilter("Text-files", "*.txt")
        );


            File choosenFile = fileChooser.showOpenDialog(null);
            if (choosenFile != null) {
                System.out.println("Your choosen file: " + choosenFile);
            }


        String xPattern = ("x = (\\d+)");
        String yPattern = ("y = (\\d+)");

        int rownumber = 5;
        int columnnumber = 0;
        int right = 0;
        try (Scanner scanner = new Scanner(choosenFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // checkin g line is empty or commented or with rule line
                if (line.isEmpty() || Pattern.matches(".*#.*", line) || Pattern.matches(".*rule.*", line)) {
                    continue;
                }
                System.out.println(line);
                // split the line with $
                Pattern p = Pattern.compile("(?<=\\$)");
                String[] items = p.split(line);
                for (String item : items) {
                    // itemTmp = 2b3o1b2o$
                    String itemTmp = item;
                    // while itemTmp is a valid form
                    while ((!itemTmp.isEmpty()) && Pattern.matches(".*b.*|.*o.*", itemTmp)) {
                        // b pattern - eg. 34b --> cnumber will be 34
                        Pattern bnumber = Pattern.compile("^(?<cnumber>\\d*?)b");
                        Matcher bmatcher = bnumber.matcher(itemTmp);
                        // o pattern eg. 3o -> onumber will be 3
                        Pattern onumber = Pattern.compile("^(?<onumber>\\d*?)o");
                        Matcher omatcher = onumber.matcher(itemTmp);

                        if (bmatcher.find()) {
                            String bNumString = bmatcher.group("cnumber");
                            int bNumInt = 1;
                            if (!bNumString.isEmpty()) {
                                bNumInt = Integer.parseInt(bNumString);
                            }
                            columnnumber = columnnumber + bNumInt;
                            itemTmp = itemTmp.replaceFirst("^\\d*?b", "");
                        } else if (omatcher.find()) {
                            String oNumString = omatcher.group("onumber");
                            int oNumInt = 1;
                            if (!oNumString.isEmpty()) {
                                oNumInt = Integer.parseInt(oNumString);
                            }
                            for (int cnum = 1; cnum <= oNumInt; cnum++) {
                                gameBoard.board[rownumber + 5 + right][columnnumber + cnum + 4] = 1;
                                //columnnumber = columnnumber +1;
                            }
                            columnnumber = columnnumber + oNumInt;
                            itemTmp = itemTmp.replaceFirst("^\\d*?o", "");
                        }

                    }
                    //if $ ONLY move to next row (row = row + 1 and column =0)
                    if (Pattern.matches(".*\\$", item)) {
                        columnnumber = 0;
                        rownumber = rownumber + 1;
                   }
                }
            }
            gameBoard.draw();
            gameBoard.drawGrid();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //clearButton funksjon som clearer brette
    // La til tl.stop(); slik at timelinen stopper når man trykker Clear
    // @Author Momcilo Delic - s315282
    public void clearButton(){
        gameBoard.ClearBtn();
        tl.stop();
        playStop.setText("Play");
        gameBoard.draw();
        gameBoard.setGrid();
    }


    //Knappfunksjonen som ved trykk bare går en generasjon videre.
    // @Author Momcilo Delic - s315282


    public void oneStep() {
        //Who needs animation when you got fast fingers :)
        nextGen();
    }

    public void setGrid(){
        gameBoard.setGrid();
    }

    //Fikset en Play/Stop knapp istedenfor å ha 2
    // @Author Momcilo Delic - s315282
    public void playStop() {
        if (tl.getStatus() == Animation.Status.RUNNING) {
            tl.stop();
            playStop.setText("Play");

        } else {
            tl.play();
            playStop.setText("Stop");
        }

    }

    // Exit game
    // @Author Momcilo Delic - s315282
    public void exitGame()
    {
        Platform.exit();
    }


}