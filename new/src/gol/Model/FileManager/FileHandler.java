package gol.Model.FileManager;


import gol.Controller.Controller;
import gol.Model.Board.DynamicBoard;
import gol.Model.Board.InterfaceBoard;
import gol.Model.FileManager.Decoders.RLEDecoder;
import gol.View.Information;
import javafx.scene.control.ColorPicker;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.sound.midi.MidiDevice;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileHandler extends RLEDecoder {



    public void chooseFile() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open GOL Shape");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Run Length Encoded File", "*.RLE"),
                new FileChooser.ExtensionFilter("All files", "*"));

    }


}
