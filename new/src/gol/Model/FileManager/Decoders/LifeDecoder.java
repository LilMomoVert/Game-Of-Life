package gol.Model.FileManager.Decoders;

import gol.Controller.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by Momcilo Delic on 4/20/2017.
 */
public class LifeDecoder extends Controller {
    public static byte[][] parsePlainText(){

        // removes meta data
        while(txtStringList.get(patterStart).startsWith("!")){
            txtStringList.remove(patterStart);
        }


        patternHeight = txtStringList.size();

        patternWidth = 0;

        // finds the width of the pattern
        for(int x = 0; x < txtStringList.size(); x++){
            if(txtStringList.get(x).length() > patternWidth){
                patternWidth = txtStringList.get(x).length();
            }
        }

        LifeBoard = new byte[patternHeight + 50][patternWidth + 50];

        // runs through the text and generates pattern
        for(int y = 0; y < patternHeight; y++){
            for(int x = 0; x < txtStringList.get(y).length(); x++){

                if(txtStringList.get(y).charAt(x) == 'O'){
                    LifeBoard[y][x] = (byte)1;
                }
                else{
                    LifeBoard[y][x] = (byte)0;
                }
            }
        }
        return LifeBoard;
    }

    private static List<String> readLinesFromFile(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }
}