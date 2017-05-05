package gol.Controller;

import gol.Model.Board.StaticBoard;
import gol.Model.FileManager.Decoders.RLEDecoder;
import gol.Model.GameThreads;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
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
 * -----------  Software development Game Of Life ------------
 * This project is an implementation of the popular animation/game
 * called Game of Life by Conway. The game is implemented in Java.
 * <p>
 * The project started out with 3 group members and sadly, ended with only 1.
 * This made this project a lot of harder for me in many ways. I have never programmed
 * before and there was a lot to learn, from RegEX and RLE and all the way to ArrayList.
 * I really hope this goes under consideration when the project is getting graded.
 * Overall i really enjoyed the project and learned a lot. I was really hoping to get in touch
 * with 3D programming, but sadly i did not get time for it.
 */

/**
 * What can be improved with the Controller?
 *
 * - The program currently only has one Controller and it would be better to split it into several Controllers
 * For instance, MenuController, CanvasController, Master Controller. Didn't get time to do this.
 */

// -"TODO:"- is used to show what can be improved in the code

public class Controller implements Initializable {

    //=========================================================================//
    //                             @FXML                                       //
    //=========================================================================//
    @FXML
    protected Button oneStep;
    @FXML
    protected Button playStop;
    @FXML
    protected Slider sizeSlider;
    @FXML
    protected Slider speedSlider;
    @FXML
    protected ColorPicker cellColor;
    @FXML
    protected ColorPicker backgroundColor;
    @FXML
    protected ColorPicker gridColor;
    @FXML
    protected CheckBox checkcircle;
    @FXML
    protected CheckBox dynamicSize;
    @FXML
    protected ComboBox rulecell;
    @FXML
    protected RadioButton staticButton;
    @FXML
    protected RadioButton dynamicButton;
    @FXML
    protected Canvas theCanvas;
    //=========================================================================//
    //                             Variables                                   //
    //=========================================================================//

    private double cellSize;
    protected byte[][] board;
    private GraphicsContext gc;
    private boolean circle;
    private boolean dynamicsize = false;

    //=========================================================================//
    //                             References                                  //
    //=========================================================================//
    private Information info;
    private InterfaceBoard gameBoard;
    private GameThreads threads;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Setting variables with what they should do when they are initialized
        cellSize = 15;
        dynamicSize.setSelected(true);
        dynamicSize.setText("Dynamic Size (ON)");
        checkcircle.setText("Circle Shape Cell (OFF)");
        dynamicButton.setSelected(true);

        // GraphicsContext
        gc = theCanvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, theCanvas.getWidth(), theCanvas.getHeight());


        // Initializing start objects
        info = new Information();
        gameBoard = new DynamicBoard(gc, cellSize, 45, 60);
        threads = new GameThreads();
        initMethods();
    }

    private void initMethods() {
        // Setting default color for the Colorpickers
        backgroundColor.setValue(WHITE);
        gridColor.setValue(BLACK);
        cellColor.setValue(RED);

        // Setting the ColorPickers
        gameBoard.setCellColor(cellColor);
        gameBoard.setGridColor(gridColor);
        gameBoard.setBackgroundColor(backgroundColor);

        // Calling the listeners in initialize
        // so they start listening from the start
        gameBoard.cellColorPicker();
        gameBoard.backgroundColorPicker();
        gameBoard.gridColorPicker();
        sizeSlider();
        speedSlider();

        // If the mouse is on canvas, the canvas will be focused
        // This solves the problem where the keys won't work on the game
        theCanvas.addEventFilter(MouseEvent.ANY, (event) -> theCanvas.requestFocus());

        // Combobox that lets the user decide which rules he want to use
        // The game start with normal rules
        rulecell.setValue("Normal Rules");
        rulecell.getItems().setAll("Normal Rules", "Fast Population Rule", "Improvized Rule");

        // Draws the board
        gameBoard.draw();
    }

    /**
     * - Check-box
     * Checks if the circle boolean is true/false
     * and afterwards sends the value to gameBoard
     * @author Momcilo Delic - s315282
     */
    public void checkcircle() {
        if (checkcircle.isSelected()) {
            circle = true;
            checkcircle.setText("Circle Shape Cell (ON)");
        } else {
            circle = false;
            checkcircle.setText("Circle Shape Cell (OFF)");
        }
        gameBoard.setCircle(circle);
    }


    /**
     * - Check-box
     * Checks if the dynamicsize boolean is
     * true/false and afterwards sends the value to
     * gameboard
     * @author Momcilo Delic - s315282
     */
    public void dynamicSize() {
        if (dynamicSize.isSelected()) {
            dynamicsize = true;
            dynamicSize.setText("Dynamic Size (ON)");
        } else {
            dynamicsize = false;
            dynamicSize.setText("Dynamic Size (OFF)");
        }
        gameBoard.setDynamicSize(dynamicsize);
    }


    /**
     * nextGenRule is a method that sets the game rule
     * from the user input in ComboBox, default rules are
     * the standard Conway Rules
     * @author Momcilo Delic - s315282
     */
    public void nextGenRule() {
        {
            switch (rulecell.getValue().toString()) {
                case "Normal Rules":
                    gameBoard.nextGeneration(0, gameBoard.getHeight());
                    break;
                case "Fast Population Rule":
                    gameBoard.fastPopulateRule();
                    break;
                case "Improvized Rule":
                    gameBoard.ImprovizedRule();
                    break;
            }
        }
    }


    /**
     * PressKey is a
     * "onKeyPress" called on the Canvas
     * When you hover over the canvas, you can move
     * the pattern or stop the game by pressing space
     * @param kevent
     */
    public void PressKey(KeyEvent kevent) {
        switch (kevent.getCode()) {
            case A:
                try {
                    gameBoard.patternLeft();
                } catch (IndexOutOfBoundsException ite) {
                    info.ErrorStaticBoard();
                }
                break;
            case S:
                try {
                    gameBoard.patternDown();
                } catch (IndexOutOfBoundsException ite) {
                    info.ErrorStaticBoard();
                }
                break;
            case D:
                try {
                    gameBoard.patternRight();
                } catch (IndexOutOfBoundsException ite) {
                    info.ErrorStaticBoard();
                }
                break;
            case W:
                try {
                    gameBoard.patternUp();
                } catch (IndexOutOfBoundsException ite) {
                    info.ErrorStaticBoard();
                }
                break;
            case SPACE:
                playStop();
                break;
        }
    }


    /**
     * clear method that clears the specified
     * rectangle, sets color to White and then fills the
     * rectangle with the specified height/width
     * @author Momcilo Delic - s315282
     */
    private void clear() {
        gc.clearRect(0, 0, theCanvas.getWidth(), theCanvas.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, theCanvas.getWidth(), theCanvas.getHeight());
    }

    /**
     *  - Radiobutton to Static
     *  When Radiobutton static is selected
     *  the interface refers to the StaticBoard class
     *
     *  When the dynamicBoard is used and changed to
     *  Static StaticBoard, it remembers the board
     *  converts it to byte and sets the static board
     *  This won't delete the pattern and the user can use
     *  the same pattern for the Static Board
     *
     *  The object is created, and afterwards
     *  the initMethods are called.
     *
     *  After initMethods, I changed some of the values
     *  that i want to be different for the Static StaticBoard.
     *
     *  @author Momcilo Delic - s315282
     */
    public void changeStat() {
        if (!staticButton.isSelected()) {
            staticButton.setSelected(true);
        } else {
            dynamicButton.setSelected(false);
            // Converts ArrayList to byte [][]
            byte[][] convertedArray = gameBoard.getByteArray();
            // Saves the oldCellSize from dynamic board
            double oldCellSize = gameBoard.getCellSize();
            // Saves the old height and with from dynamicBoard and uses it for the static
            // This will keep the board the same size
            int oldWidth = gameBoard.getHeight();
            int oldHeight = gameBoard.getWidth();
            gameBoard = new StaticBoard(gc, cellSize, oldWidth, oldHeight);
            clear();
            // Sets the convertedArray
            gameBoard.setgameBoard(convertedArray);
            initMethods();
            dynamicsize = false;
            dynamicSize.setSelected(false);
            // Setting the oldCellSize to size Slider value so the
            // slider is perfectly zoomed when changed
            sizeSlider.setValue(oldCellSize);
        }
    }

    /**
     *  - Radiobutton to Dynamic
     *  When Radiobutton DynamicBoard is selected
     *  the interface refers to the DynamicBoard class
     *
     *  After the object is created, the
     *  initMethods are called.
     *
     *  @author Momcilo Delic - s315282
     */
    public void changeDyn() {
        if (!dynamicButton.isSelected()) {
            dynamicButton.setSelected(true);
        } else {
            staticButton.setSelected(false);
            byte[][] oldboard = gameBoard.getByteArray();
            gameBoard = new DynamicBoard(gc, cellSize, 45, 60);
            gameBoard.setgameBoard(oldboard);
            initMethods();
            dynamicsize = true;
            dynamicSize.setSelected(true);
        }
    }


    /**
     * Slider listener that listens to the slider
     * and controls the timeline speed.
     *
     * The listener is called in the initialize so it can
     * start listening from the start of the program
     * @author Momcilo Delic - s315282
     */
    private void speedSlider() {
        speedSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                tl.setRate(speedSlider.getValue());
                speedSlider.setMin(1);
                //System.out.println(speedSlider.getValue());
            }
        });
    }

    /**
     * Slider listener that controls the cellSize value
     * The listener is called in the initialize so it listens from the start
     *
     * Size slider won't work when the "dynamicSize" is turned off and the
     * user will be informed about that with an alert message
     *
     * @author Momcilo Delic - s315282
     */
    private void sizeSlider() {
        sizeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (dynamicSize.isSelected()) {
                    tl.stop();
                    playStop.setText("Play");
                    info.Error();
                    tl.play();
                    playStop.setText("Stop");
                } else {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(0, 0, theCanvas.getWidth(), theCanvas.getHeight());
                    gameBoard.setCellSize(sizeSlider.getValue());
                    gameBoard.setCellSize(cellSize);
                    cellSize = sizeSlider.getValue();
                    sizeSlider.setMin(0.2);
                    sizeSlider.setMax(50);
                    gameBoard.draw();
                }
            }
        });
    }

    /**
     * This method lets the user zoom out
     * The method is not complete as intended.
     *
     * TODO: Let the user zoom in and out in a more efficient way
     *
     */
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
     * Runs nextgeneration in the timeline
     * Timeline jumps to start to the duration ZERO
     * Duration is set to 200 millis
     * This means nextgeneration will happend every 200 milliseconds
     *
     * @author Momcilo Delic - s315282
     */
    private Timeline tl;

    {
        tl = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            nextGen();
//            int availableProcessors = Runtime.getRuntime().availableProcessors();
//            for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
//
//                int start1 = gameBoard.getHeight()*i / availableProcessors;
//                int stop1 =  start1 + gameBoard.getHeight() / availableProcessors;
//
//                threads.addThreads(() -> {
//                    gameBoard.nextGeneration(start1, stop1);
//                });
//
//                int start2 = gameBoard.getHeight()*i / availableProcessors;
//                int stop2 =  start2 + gameBoard.getHeight()*i / availableProcessors;
//
//                threads.addThreads(() -> {
//                    gameBoard.nextGeneration(start2, stop2);
//                });
//
//                int start3 = gameBoard.getHeight()*i / availableProcessors;
//                int stop3 =  start3 + gameBoard.getHeight()*i / availableProcessors;
//
//                threads.addThreads(() -> {
//                    gameBoard.nextGeneration(start3, stop3);
//                });
//
//                int start4 = gameBoard.getHeight()*i / availableProcessors;
//                int stop4 =  start4 + gameBoard.getHeight()*i / availableProcessors;
//
//                threads.addThreads(() -> {
//                    gameBoard.nextGeneration(start4, stop4);
//                });
//            }
//                try {
//                    threads.runThreads();
//                } catch (InterruptedException ee) {
//                    threads.clearWorkers();
//            }
            tl.stop();
            tl.setRate(Math.abs(tl.getRate()));
            tl.jumpTo(Duration.ZERO);
            tl.play();
        }));
    }

    /**
     * About the game
     * @author Momcilo Delic - s315282
     */
    public void about() {
        info.aboutGOL();
    }

    /**
     * Made by
     * @author Momcilo Delic - s315282
     */
    public void madeBy() {
        info.authorInfo();
    }

    /**
     * Draw cells on the board
     * If mouse dragged, sets the cells to alive (1)
     * If controll is down and dragged
     * it will kill alive cells when dragged over (0)
     *
     * @author Momcilo Delic - s315282
     *
     * TODO: An improvment would be to make it possible for the user to move the board around
     * TODO: by dragging the mouse. Add this function when the Shift is down so the draw
     * TODO: function and the move board function don't crash together
     */
    public void MouseDraw(MouseEvent event) {
        int getX = (int) (event.getX() / gameBoard.getCellSize());
        int getY = (int) (event.getY() / gameBoard.getCellSize());
        try {
            if (event.isControlDown()) {

                if (gameBoard.getLive(getY, getX) == 1) {
                    gameBoard.setLive(getY, getX, (byte) 0);
                }
                gameBoard.draw();
            } else {
//                if (gameBoard instanceof DynamicBoard) {
//                    int missingHeight = getY - gameBoard.getWidth();
//                    if (missingHeight > 0)
//                        for (int i = 0; i < missingHeight; i++) {
//                            ((DynamicBoard) gameBoard).addDown();
//                        }
//
//                    int missingWidth = getX - gameBoard.getHeight();
//                    if (missingWidth > 0)
//                        for (int i = 0; i < missingWidth; i++) {
//                            ((DynamicBoard) gameBoard).addRight();
//                        }
//                }
                if (gameBoard.getLive(getY, getX) == 0) {
                    gameBoard.setLive(getY, getX, (byte) 1);
                }
                gameBoard.draw();

            }
        } catch (IndexOutOfBoundsException ioeb) {
            System.out.println("Out of the game board..");
        }
    }

    public void MouseClick(MouseEvent event) {
        int getX = (int) (event.getX() / gameBoard.getCellSize());
        int getY = (int) (event.getY() / gameBoard.getCellSize());

        try {
            if (event.isControlDown()) {


                if (gameBoard.getLive(getY, getX) == 1) {
                    gameBoard.setLive(getY, getX, (byte) 0);
                }
                gameBoard.draw();
            } else {

                if (gameBoard.getLive(getY, getX) == 0) {
                    gameBoard.setLive(getY, getX, (byte) 1);
                }
                gameBoard.draw();

            }
        } catch (IndexOutOfBoundsException ioeb) {
            System.out.println("Clicked outside of the board");
        }
    }


    /**
     * New stage created when the "Instruction" button is called.
     * Connecting the loader with another fxml-
     *
     * @author Momcilo Delic - s315282
     */
    public void Instructions() throws Exception {
        try {
            Stage GameInstructions = new Stage();
            GameInstructions.getIcons().add(new Image("img/monster.png"));
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/HowToPlay.fxml"));
            AnchorPane apane = fxmlLoader.load();
            Scene newScene = new Scene(apane, 677, 400);
            GameInstructions.setScene(newScene);
            GameInstructions.setTitle("Spillfunksjoner");
            GameInstructions.show();
        } catch (Exception e) {
            info.Ops();
        }
    }

    /**
     * Gets the value(rule) selected from the
     * the ComboBox and uses it as the nextGeneration rule
     *
     * @author Momcilo Delic - s31582
     */
    private void nextGen() {
        nextGenRule();
    }

    /**
     * Button to generate Random generations
     * @author Momcilo Delic
     */
    public void randomGame() {
        gameBoard.Randomness();
        gameBoard.draw();
    }

    /**
     * Really simple FileChooser that lets the user
     * loard Run Lenght Encoding Files (RLE)
     * @throws IOException this tells the compiler
     * that this method need special threatment/attention
     * and it will handle it or throw it back.
     * @author Momcilo Delic - s315282
     *
     *  TODO: Move to another class and maybe make a PlainText decoder.
     */
    public void fileChooser() throws IOException {
        RLEDecoder parser = new RLEDecoder();
        FileChooser patternChooser = new FileChooser();
        patternChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        patternChooser.setTitle("Choose your file");
        patternChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("RLE Files", "*.rle"));

        File selectedFile = patternChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {

            try {
                gameBoard.ClearButton();
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
    public void clearButton() {
        gameBoard.ClearButton();
        tl.stop();
        playStop.setText("Play");
        gameBoard.draw();
    }

    /**
     * When you click the button
     * it goes only one generation
     * @author Momcilo Delic - s315282
     */
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
    public void playStop() {
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
    public void exitGame() {
        Platform.exit();
    }

}