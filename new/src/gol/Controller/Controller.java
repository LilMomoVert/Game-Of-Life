package gol.Controller;

import gol.Model.Board.Board;
import gol.Model.FileManager.Decoders.RLEDecoder;
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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
    @FXML public Slider                 sizeSlider;
    @FXML public Slider                 speedSlider;
    @FXML public ColorPicker            cellColor;
    @FXML public ColorPicker            backgroundColor;
    @FXML public ColorPicker            gridColor;
    @FXML public CheckBox               checkcircle;
    @FXML public CheckBox               dynamicSize;
    @FXML public ComboBox               rulecell;
    @FXML public RadioButton            staticButton;
    @FXML public RadioButton            dynamicButton;
    @FXML public Canvas                 theCanvas;
    //=========================================================================//
    //                             Variables                                   //
    //=========================================================================//

    private double                      cellSize;
    public byte[][]                     board;
    public GraphicsContext              gc;
    private boolean                     circle = false;
    private boolean                     dynamicsize = false;


    //=========================================================================//
    //                             References                                  //
    //=========================================================================//
    private Information                         info;
    private InterfaceBoard                      gameBoard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cellSize = 15;
        dynamicSize.setSelected(true);
        dynamicButton.setSelected(true);

        gc = theCanvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, theCanvas.getWidth(), theCanvas.getHeight());

        // Initialize objects
        info = new Information();
        gameBoard = new DynamicBoard(gc, cellSize, 45, 60);

        initMethods();
    }

    public void initMethods(){
        // Setting default color for the Colorpickers
        backgroundColor.setValue(WHITE);
        gridColor.setValue(BLACK);
        cellColor.setValue(RED);

        // S
        gameBoard.setCellColor(cellColor);
        gameBoard.setGridColor(gridColor);
        gameBoard.setBackgroundColor(backgroundColor);

        sizeSlider.setValue(17);
        sizeSlider();

        gameBoard.cellColorPicker();
        gameBoard.backgroundColorPicker();
        gameBoard.gridColorPicker();
        speedSlider();

        theCanvas.addEventFilter(MouseEvent.ANY, (e) -> theCanvas.requestFocus());

        // Combobox
        rulecell.setValue("Normal Rules");
        rulecell.getItems().setAll("Normal Rules", "Fast Growth", "Improvized Rule");

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
                case "Improvized Rule":
                    gameBoard.ImprovizedRule();
                    break;
            }
        }
    }

    public void PressKey(KeyEvent event){
        switch (event.getCode()){
            case A:
                patternLeft();
                gameBoard.draw();
                break;
            case S:
                patternDown();
                gameBoard.draw();
                break;
            case D:
                patternRight();
                gameBoard.draw();
                break;
            case W:
                patternUp();
                gameBoard.draw();
                break;

        }
    }


    public void changeStat() {
        gc.clearRect(0,0, theCanvas.getWidth(), theCanvas.getHeight());
        if (!staticButton.isSelected()){
            staticButton.setSelected(true);
        }
        else{
            dynamicButton.setSelected(false);
            staticBoard();
            initMethods();
            clearButton();
        }
    }

    public void changeDyn() {
        gc.clearRect(0,0, gameBoard.getHeight(), gameBoard.getWidth());
        if (!dynamicButton.isSelected()){
            dynamicButton.setSelected(true);
        }
        else{
            staticButton.setSelected(false);
            dynamicBoard();
            initMethods();
            clearButton();
        }
    }

    private void staticBoard(){
        gameBoard = new Board(gc,cellSize, 47, 62);
    }

    private void dynamicBoard(){
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
                    sizeSlider.setMin(1);
                    sizeSlider.setMax(50);
                    gameBoard.draw();
                    System.out.println(cellSize);
                }
            }
        });
    }

    public void onScroll(ScrollEvent sEvent) {
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

    /**
     * Timeline jumps to start to the duration ZERO
     * Duration is set to 200 millis
     */
    public Timeline tl;{
        tl = new Timeline(new KeyFrame(Duration.millis(200), event -> {
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


    /**
     * 
     */
    public void patternUp(){
            try {
                gameBoard.patternUp();
            } catch (IndexOutOfBoundsException ite) {
                info.ErrorStaticBoard();
            }
    }

    public void patternDown(){
        try {
            gameBoard.patternDown();
        } catch (IndexOutOfBoundsException ite){
            info.ErrorStaticBoard();
        }
    }

    public void patternLeft(){
        try {
            gameBoard.patternLeft();
        } catch (IndexOutOfBoundsException ite){
            info.ErrorStaticBoard();
        }
    }

    public void patternRight(){
        try {
            gameBoard.patternRight();
        } catch (IndexOutOfBoundsException ite){
            info.ErrorStaticBoard();
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
        RLEDecoder parser = new RLEDecoder();
        FileChooser patternChooser = new FileChooser();
        patternChooser.setTitle("Choose your file");
        patternChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("RLE Files", "*.rle"));

        File selectedFile = patternChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {

            try {
                parser.readFile(selectedFile);
                gameBoard.setgameBoard(parser.getBoard());

            } catch (IOException e) {
                info.Ops();
            }
            gameBoard.draw();
        }
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
        //Who needs animation if you have fast fingers :)
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