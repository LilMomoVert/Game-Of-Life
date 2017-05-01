package gol.Controller;

import gol.Model.Board.Board;
import gol.Model.FileManager.Decoders.LifeDecoder;
import gol.Model.FileManager.Decoders.RLEDecoder;
import gol.Model.FileManager.FileHandler;
import gol.View.Information;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
    @FXML public CheckBox               dynamicSize;
    @FXML public ComboBox               rulecell;
    @FXML public RadioButton            staticButton;
    @FXML public RadioButton            dynamicButton;
    public Slider sizeSlider;
    //=========================================================================//
    //                             Variables                                   //
    //=========================================================================//

    private double                     cellSize;
    protected static int               patternHeight;
    protected static int               patternWidth;
    protected static final byte        patterStart = 0;
    public byte[][]                    board;
    protected static byte[][] LifeBoard;
    public Canvas                      theCanvas;
    public Scene                       scene;
    public GraphicsContext             gc;
    protected static List<String>      txtStringList;
    private boolean                    circle = false;
    private boolean                    dynamicsize = false;


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
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, theCanvas.getWidth(), theCanvas.getHeight());

        info = new Information();
        handler = new FileHandler();
        dynamicSize.setSelected(true);

        dynamicBoard();

        backgroundColor.setValue(WHITE);
        gridColor.setValue(BLACK);
        cellColor.setValue(RED);

        gameBoard.setCellColor(cellColor);
        gameBoard.setGridColor(gridColor);
        gameBoard.setBackgroundColor(backgroundColor);

        sizeSlider();

        gameBoard.cellColorPicker();
        gameBoard.backgroundColorPicker();
        gameBoard.gridColorPicker();
        speedSlider();
        rulecell.setValue("Normal Rules");
        rulecell.getItems().setAll("Normal Rules", "Fast Growth", "EPILEPSY ATTACK!");

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

    public void dynamicSize(){
        if(dynamicSize.isSelected()){
            dynamicsize = true;
            dynamicSize.setText("Dynamic Size ON");
        } else {
            dynamicsize = false;
            dynamicSize.setText("Dynamic Size OFF");
        }
        gameBoard.setDynamicSize(dynamicsize);

        if(dynamicSize.isSelected() == false){
        }
    }

    public void nextGenRule() {
        {
            switch (rulecell.getValue().toString()) {
                case "Normal Rules":
                    gameBoard.nextGeneration();
                    break;
                case "Fast Growth":
                    gameBoard.CoolRandomRuleShapeWithAnAwesomeName();
                    break;
                case "EPILEPSY ATTACK!":
                    gameBoard.epilepsyAttack();
                    break;
            }
        }
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

    public void sizeSlider(){
        sizeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if(dynamicSize.isSelected()){
                    tl.stop();
                    playStop.setText("Play");
                    info.Error();
                    tl.play();
                    playStop.setText("Stop");
                } else {
                    gameBoard.setCellSize(sizeSlider.getValue());
                    gameBoard.setCellSize(cellSize);
                    cellSize = sizeSlider.getValue();
                    sizeSlider.setMax(20);
                    gameBoard.draw();
                    System.out.println(cellSize);
                }
            }
        });
    }

    public void onScroll(ScrollEvent sEvent) {
        if(sEvent.isControlDown()) {
            double zoomValue = 1.5;

            if (sEvent.getDeltaY() <= 0) {
                zoomValue = 1 / zoomValue;
            }

            double scale = theCanvas.getScaleX();
            double newScale = scale * zoomValue;
            if (newScale < 1.5 && newScale > 0.2) {

                theCanvas.setScaleX(newScale);
                theCanvas.setScaleY(newScale);
            }
        }
    }

    /**
     * Timeline jumps to start to the duration ZERO
     * Duration is set to 200 millis
     */
    public Timeline tl;{
        tl = new Timeline(new KeyFrame(Duration.millis(200), event -> {;
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


    public void MouseDraw(MouseEvent event) {
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
        nextGenRule();
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
                readLinesFromFile(selectedFile);
                gameBoard.setgameBoard(LifeDecoder.parsePlainText());

            } catch (IOException e) {
                info.Ops();
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