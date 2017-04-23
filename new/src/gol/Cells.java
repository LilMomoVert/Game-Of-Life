package gol;

/**
 * Created by Kani Boyka on 4/20/2017.
 */
public class Cells extends Controller {
    static byte[][] parsePlainText(){

        // removes meta data
        while(txtStringList.get(patterStart).startsWith("!")){
            txtStringList.remove(patterStart);
        }


        fileHeight = txtStringList.size();

        fileWidth = 0;

        // finds the width of the pattern
        for(int x = 0; x < txtStringList.size(); x++){
            if(txtStringList.get(x).length() > fileWidth){
                fileWidth = txtStringList.get(x).length();
            }
        }

        byteArray = new byte[fileHeight][fileWidth];

        // runs through the text and generates pattern
        for(int y = 0; y < fileHeight; y++){
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