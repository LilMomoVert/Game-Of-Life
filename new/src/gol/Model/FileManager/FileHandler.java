package gol.Model.FileManager;


import gol.Model.FileManager.Decoders.LifeDecoder;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileHandler {
    static List<String> contentStringList;
    static int patternHeight;
    static int patternWidth;
    static final byte BEGGINING_LINE = 0;
    static byte[][] patternArray;
    public Stage stage;

    public void chooseFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open GOL Shape");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Run Length Encoded File", "*.RLE"),
                new FileChooser.ExtensionFilter("Text File", "*.txt"),
                new FileChooser.ExtensionFilter("All files", "*")

        );

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            LifeDecoder.parsePlainText();
        }
    }
}
