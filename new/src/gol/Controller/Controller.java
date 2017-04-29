package gol.Controller;

import gol.Model.Board.Board;
import gol.Model.FileManager.Decoders.RLEDecoder;
import gol.Model.FileManager.FileHandler;
import gol.View.Information;
import gol.Model.FileManager.Decoders.LifeDecoder;
import gol.Model.Board.DynamicBoard;
import gol.Model.Board.InterfaceBoard;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;

import static javafx.scene.paint.Color.*;

/**
 * Created by Momcilo Delic on 2/10/2017.
 */

public class Controller implements Initializable {

    //=========================================================================//
    //                             @FXML                                       //
    //=========================================================================//
    @FXML public Button                 oneStep;
    @FXML public Button                 playStop;
    @FXML public Button                 patternUp;
    @FXML public Slider                 speedSlider;
    @FXML public ColorPicker            cellColor;
    @FXML public ColorPicker            backgroundColor;
    @FXML public ColorPicker            gridColor;
    @FXML public CheckBox               checkcircle;
    @FXML public RadioButton            staticButton;
    @FXML public RadioButton            dynamicButton;
    //=========================================================================//
    //                             Variables                                   //
    //=========================================================================//

    private double                     cellSize;
    protected static int               patternHeight;
    protected static int               patternWidth;
    private int                        offsetY = 0;
    private int                        offsetX = 0;
    protected static final byte        patterStart = 0;
    public byte[][]                    board;
    protected static byte[][]          byteArray;
    public Canvas                      theCanvas;
    public Scene                       scene;
    public GraphicsContext             gc;
    protected static List<String>      txtStringList;
    private int                        canvasDisplacedY;
    private int                        canvasDisplacedX;
    private boolean                    circle = false;


    //=========================================================================//
    //                             References                                  //
    //=========================================================================//
    Information                        info;
    FileHandler                        handler;
    InterfaceBoard                     gameBoard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cellSize = 15;

        gc = theCanvas.getGraphicsContext2D();

        info = new Information();
        handler = new FileHandler();

        dynamicBoard();

        canvasDisplacedX = 0;
        canvasDisplacedY = 0;

        backgroundColor.setValue(WHITE);
        gridColor.setValue(BLACK);
        cellColor.setValue(RED);

        gameBoard.setCellColor(cellColor);
        gameBoard.setGridColor(gridColor);
        gameBoard.setBackgroundColor(backgroundColor);

        gameBoard.cellColorPicker();
        gameBoard.backgroundColorPicker();
        gameBoard.gridColorPicker();
        speedSlider();

        // Set GraphicsContexts
        gc = theCanvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, theCanvas.getWidth(), theCanvas.getHeight());
        //Drawing the board
        gameBoard.draw();
    }

    public void checkcircle(){
        if(checkcircle.isSelected()){
            circle = false;
        } else {
            circle = true;
        }
        gameBoard.setCircle(circle);
    }

    public void changeStat() {
        if (!staticButton.isSelected()){
            staticButton.setSelected(true);
        }
        else{
            dynamicButton.setSelected(false);
            staticBoard();
        }
    }

    public void changeDyn() {
        if (!dynamicButton.isSelected()){
            dynamicButton.setSelected(true);
        }
        else{
            staticButton.setSelected(false);
            dynamicBoard();
        }
    }

    @FXML
    void staticBoard(){

        gameBoard = new Board(gc,cellSize, 45, 60);

    }

    @FXML
    void dynamicBoard(){
        gameBoard = new DynamicBoard(gc,cellSize, 45, 60);

    }

    public void momo(){
            gameBoard = new DynamicBoard(gc, cellSize, 50, 70);
    }

    // Speed listener
    public void speedSlider(){
        speedSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                setSpeed(speedSlider.getValue());
                speedSlider.setMin(1);
                //System.out.println(speedSlider.getValue());
            }
        });
    }

    public void scrollHandler(ScrollEvent e) {

        double factor = 1.5;

        if (e.getDeltaY() <= 0) {
            factor = 1 / factor;
        }

        double scale = theCanvas.getScaleX();
        double newScale = scale * factor;
        if (newScale < 1.5 && newScale > 0.2) {

            theCanvas.setScaleX(newScale);
            theCanvas.setScaleY(newScale);
        }
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
        System.out.println("hei");
    }

    /**
     * Timeline jumps to start to the duration ZERO
     * Duration is set to 200 millis
     */
    public Timeline tl;{
        tl = new Timeline(new KeyFrame(Duration.millis(300), event -> {;
            nextGen();
            tl.stop();
            tl.setRate(Math.abs(tl.getRate()));
            tl.jumpTo(Duration.ZERO);
            tl.play();
        }));
    }

    public void about(){
        info.aboutGOL();
    }

    public void madeBy(){
        info.authorInfo();
    }

    public void setSpeed(double speed){
        tl.setRate(speed);
    }

    public void handleMouseEntered(MouseEvent event) {
        offsetX = (int) event.getX();
        offsetY = (int) event.getY();
    }

    public int getCanvasDisplacedX() {
        return canvasDisplacedX;
    }

    public int getCanvasDisplacedY() {
        return canvasDisplacedY;
    }

    public void setCanvasDisplacedX(int x) {
        canvasDisplacedX = x;
    }

    public void setCanvasDisplacedY(int y) {
        canvasDisplacedY = y;
    }

    public void movePosition(int x, int y) {
        setCanvasDisplacedX(canvasDisplacedY + x);
        setCanvasDisplacedY(canvasDisplacedY + y);
    }

    public void MouseDraw(MouseEvent event) {
//            movePosition(offsetX - (int) event.getX(), offsetY - (int) event.getY());
//            offsetX = (int) event.getX();
//            offsetY = (int) event.getY();
//            System.out.println(offsetX);

        if(event.isControlDown()) {
            try {
                int x = (int) (event.getX() / gameBoard.getCellSize());
                int y = (int) (event.getY() / gameBoard.getCellSize());

                if (gameBoard.getLive(y, x) == 1) {
                    gameBoard.setLive(y ,x, (byte) 0);
                }
                gameBoard.draw();
            } catch (IndexOutOfBoundsException ioeb){
                info.AOBCanvas();
            }
        } else {
            try {
                int x = (int) (event.getX() / gameBoard.getCellSize());
                int y = (int) (event.getY() / gameBoard.getCellSize());

                if (gameBoard.getLive(y, x) == 0) {
                    gameBoard.setLive(y, x, (byte) 1);
                }
                gameBoard.draw();
            } catch (IndexOutOfBoundsException ioeb) {
                info.AOBCanvas();
            }
        }
    }

    public void Instructions(){
        info.instructions();
    }

    public void MouseClick(MouseEvent event) {
        if(event.isControlDown()){
            try {
                int x = (int) (event.getX() / gameBoard.getCellSize());
                int y = (int) (event.getY() / gameBoard.getCellSize());

                if (gameBoard.getLive(y, x) == 1) {
                    gameBoard.setLive(y ,x, (byte) 0);
                }
                gameBoard.draw();
            } catch (IndexOutOfBoundsException ioeb){
                info.AOBCanvas();
            }
        } else {
            try {
                int x = (int) (event.getX() / gameBoard.getCellSize());
                int y = (int) (event.getY() / gameBoard.getCellSize());

                if (gameBoard.getLive(y, x) == 0) {
                    gameBoard.setLive(y ,x, (byte) 1);
                }
                gameBoard.draw();
            } catch (IndexOutOfBoundsException ioeb){
                info.AOBCanvas();
            }
        }
    }

    public void nextGen(){
        gameBoard.nextGeneration();
    }

    public void patternUp(){
            try {
                gameBoard.patternUp();
            } catch (IndexOutOfBoundsException ite) {
                info.ErrorMoveOut();
            }
    }

    public void patternDown(){
        try {
            gameBoard.patternDown();
        } catch (IndexOutOfBoundsException ite){
            info.ErrorMoveOut();
        }
    }

    public void patternLeft(){
        try {
            gameBoard.patternLeft();

        } catch (IndexOutOfBoundsException ite){
            info.ErrorMoveOut();
        }
    }

    public void patternRight(){
        try {
            gameBoard.patternRight();
        } catch (IndexOutOfBoundsException ite){
            info.ErrorMoveOut();
        }
    }

    /**
     * Button to generate Random generations
     * @author Momcilo Delic
     */
    public void randomGame(){
        gameBoard.Randomness();
        gameBoard.draw();
    }

    // TODO: Må flyttes til FileHandler klassen
    public void fileChooser() throws IOException {

        FileChooser patternChooser = new FileChooser();
        patternChooser.setTitle("Velg fil");


        patternChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(".RLE Extensions", "*.RLE"),
                new FileChooser.ExtensionFilter("Plain Text", "*.cells"));
        //ByteArrayInputStream inputStream = new ByteArrayInputStream();

        File selectedFile = patternChooser.showOpenDialog(new Stage());
//        if(test != null){
//            txtStringList = readLinesFromFile(test);
//            if(test.toString().endsWith(".rle")) {
//                // return RleParser.readFile(file);
//            }
//            else if(test.toString().endsWith(".cells")){
//                return LifeDecoder.parsePlainText();
//            }
//
//        }
//        return null;

//        File selectedFile = patternChooser.showOpenDialog();
        if (selectedFile != null) {
            System.out.println("Controller.fileOpener:" + selectedFile);

            RLEDecoder parser = new RLEDecoder();
            try {
                parser.readGameBoardFromDisk(selectedFile);
                if (true) {
                     gameBoard.setgameBoard(parser.getBoard());
                }

            } catch (IOException e) { //spytter ut eventuelle feilmedling
                System.out.println(e.getMessage());
            }
            System.out.println("Loaded File " + selectedFile);
            gameBoard.draw();
        }
    }

    private static List<String> readLinesFromFile(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }

    /**
     * Clears the board, sets the timeline to stop
     * and draws new clean board.
     * @author Momcilo Delic - s315282
     */
    public void clearButton(){
        gameBoard.ClearButton();
        tl.stop();
        playStop.setText("Play");
        gameBoard.draw();
    }

    /**
     * When you click the button
     * it goes ony one generation
     * @author Momcilo Delic - s315282
     */
    @FXML
    public void oneStep() {
        //Who needs animation when you got fast fingers :)
        nextGen();
        gameBoard.draw();
    }

    /**
     * Play/Stop button
     * If the Animation is running the Button is set to Stop
     * If the Animation is not running the Button is set to Play
     * @author Momcilo Delic - s315282
     */
    @FXML
    public void playStop()  {
        if (tl.getStatus() == Animation.Status.RUNNING) {
            tl.stop();
            playStop.setText("Play");

        } else {
            tl.play();
            playStop.setText("Stop");
        }
    }

    /**
     * Exit the game
     */
    @FXML
    public void exitGame()
    {
        Platform.exit();
    }

}










/* TODO: Fikse på senere

//        int rownumber = 5;
//        int columnnumber = 0;
//
//        try (Scanner scanner = new Scanner(choosenFile)) {
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine();
//                // checkin g line is empty or commented or with rule line
//                if (line.isEmpty() || Pattern.matches(".*#.*", line) || Pattern.matches(".*rule.*", line)) {
//                    continue;
//                }
//                System.out.println(line);
//                // split the line with $
//                Pattern p = Pattern.compile("(?<=\\$)");
//                String[] items = p.split(line);
//                for (String item : items) {
//                    // itemTmp = 2b3o1b2o$
//                    String itemTmp = item;
//                    // while itemTmp is a valid form
//                    while ((!itemTmp.isEmpty()) && Pattern.matches(".*b.*|.*o.*", itemTmp)) {
//                        // b pattern - eg. 34b --> cnumber will be 34
//                        Pattern bnumber = Pattern.compile("^(?<cnumber>\\d*?)b");
//                        Matcher bmatcher = bnumber.matcher(itemTmp);
//                        // o pattern eg. 3o -> onumber will be 3
//                        Pattern onumber = Pattern.compile("^(?<onumber>\\d*?)o");
//                        Matcher omatcher = onumber.matcher(itemTmp);
//
//                        if (bmatcher.find()) {
//                            String bNumString = bmatcher.group("cnumber");
//                            int bNumInt = 1;
//                            if (!bNumString.isEmpty()) {
//                                bNumInt = Integer.parseInt(bNumString);
//                            }
//                            columnnumber = columnnumber + bNumInt;
//                            itemTmp = itemTmp.replaceFirst("^\\d*?b", "");
//                        } else if (omatcher.find()) {
//                            String oNumString = omatcher.group("onumber");
//                            int oNumInt = 1;
//                            if (!oNumString.isEmpty()) {
//                                oNumInt = Integer.parseInt(oNumString);
//                            }
//                            for (int cnum = 1; cnum <= oNumInt; cnum++) {
//                                gameBoard.setLive(rownumber + 40,columnnumber + cnum + 40);
//                                //columnnumber = columnnumber +1;
//                            }
//                            columnnumber = columnnumber + oNumInt;
//                            itemTmp = itemTmp.replaceFirst("^\\d*?o", "");
//                        }
//
//                    }
//                    //if $ ONLY move to next row (row = row + 1 and column =0)
//                    if (Pattern.matches(".*\\$", item)) {
//                        columnnumber = 0;
//                        rownumber = rownumber + 1;
//                   }
//                }
//            }
//            gameBoard.draw();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
 */