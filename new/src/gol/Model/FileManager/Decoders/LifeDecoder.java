package gol.Model.FileManager.Decoders;

import gol.Controller.Controller;

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

        byteArray = new byte[patternHeight + 50][patternWidth + 50];

        // runs through the text and generates pattern
        for(int y = 0; y < patternHeight; y++){
            for(int x = 0; x < txtStringList.get(y).length(); x++){

                if(txtStringList.get(y).charAt(x) == 'O'){
                    byteArray[y][x] = (byte)1;
                }
                else{
                    byteArray[y][x] = (byte)0;
                }
            }
        }
        return byteArray;
    }
}