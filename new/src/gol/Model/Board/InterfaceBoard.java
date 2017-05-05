package gol.Model.Board;

import javafx.scene.control.ColorPicker;

import java.util.ArrayList;

/**
 * Created by Momcilo Delic on 4/20/2017.
 *
 * This is my InterfaceBoard interface
 * Its intended for the Static and Dynamic Board to implement it
 */

public interface InterfaceBoard {

    void setCellColor(ColorPicker cellColor);

    void setGridColor(ColorPicker gridColor);

    void setBackgroundColor(ColorPicker backgroundColor);

    void setCellSize(double value);

    void setgameBoard(byte[][] newGameBoard);

    void cellColorPicker();

    void backgroundColorPicker();

    void gridColorPicker();

    void draw();

    void nextGeneration(int start, int stop);

    void Randomness();

    void ClearButton();

    void setLive(int y, int x, byte state);

    int getWidth();

    int getHeight();

    byte getLive(int x, int y);

    void patternUp();

    void patternDown();

    void ImprovizedRule();

    void patternLeft();

    void patternRight();

    void fastPopulateRule();

    void setCircle(boolean circle);

    void setDynamicSize(boolean dynamicSize);

    double getCellSize();

    byte[][] getByteArray();
}
