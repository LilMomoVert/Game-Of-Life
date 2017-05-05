package gol.Model.FileManager.Decoders;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kani Boyka on 4/28/2017.
 */
public class RLEDecoder {

    private byte[][] board;

    public void readFile(File file) throws IOException {
        FileReader reader = new FileReader(file.getAbsoluteFile());
        BufferedReader bufferedReader = new BufferedReader(reader);
        readRLE(bufferedReader);
    }

    public void readRLE(BufferedReader BuffReader) throws IOException {
        String line;
        int x;
        int y;


        while ((line = BuffReader.readLine()) != null) {
            // if it start with x
            if (line.charAt(0) == 'x') {
                Pattern pattern = Pattern.compile("[x][ ][=][ ]([\\d]+)[,][ ][y][ ][=][ ]([\\d]+)");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    x = Integer.parseInt(matcher.group(1));
                    y = Integer.parseInt(matcher.group(1));

                    // setting the x and y to a byte[][] and adding both x and y with 20 so it won't totally wrap around the pattern
                    board = new byte[x + 20][y + 20];

                    break;
                }
                // not every rle start with x, the RLE file can in some cases start with y
            } else if(line.charAt(0) == 'y'){
                                                         // Using RegEx
                Pattern pattern = Pattern.compile("[y][ ][=][ ]([\\d]+)[,][ ][x][ ][=][ ]([\\d]+)"); // <-- this is equivalent to y = number, x = number  (its important to not forget the spaces)
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    x = Integer.parseInt(matcher.group(1));
                    y = Integer.parseInt(matcher.group(1));

                    // setting the x and y to a byte[][] and adding both x and y with 20 so it won't totally wrap around the pattern
                    board = new byte[x + 20][y + 20];

                    break;
                }
            }
        }


        int patternRows = 1;
        int patternCols = 0;

        while ((line = BuffReader.readLine()) != null) {


            Pattern pattern = Pattern.compile("([0-9]*)([$BbOo])");
            Matcher matcher = pattern.matcher(line);

            while (matcher.find()) {
                int dollar = 1;
                int d = 0;

                if (matcher.group(1).matches("\\d+")) {
                    dollar = Integer.parseInt(matcher.group(1));
                }

                while ((dollar--) > d) {
                    if (matcher.group(2).matches("[$]")) { // end of the line
                        patternRows++;
                        patternCols = 0;

                    } else if (matcher.group(2).matches("[BbOo]")) {
                        patternCols++;
                        if (matcher.group(2).matches("o")) { // cell alive
                            board[patternRows][patternCols] = (byte) 1;
                        }
                        if (matcher.group(2).matches("b")) { // cell dead
                            board[patternRows][patternCols] = (byte) 0;
                        }
                    }

                }
            }
        }
    }


    public byte[][] getBoard() {
        return board;
    }
}
