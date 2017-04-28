package gol.Model.Board;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;

/**
 * Created by Momcilo Delic on 4/20/2017.
 */

public interface InterfaceBoard {

    void setCellColor(ColorPicker cellColor);

    void setGridColor(ColorPicker gridColor);

    void setBackgroundColor(ColorPicker backgroundColor);

    void cellColorPicker();

    void backgroundColorPicker();

    void gridColorPicker();

    void draw();

    void setCellSize(double value);

    void setgameBoard(byte[][] testBoard4);

    void nextGeneration();

    void Randomness();

    void ClearButton();

    void setLive(int y, int x, byte state);

    int getHeight();

    int getWidth();

    byte getLive(int x, int y);

    void patternUp();

    void patternDown();

    void patternLeft();

    void patternRight();
}
