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
            if (line.charAt(0) == 'x') {
                Pattern pattern = Pattern.compile("[x][ ][=][ ]([\\d]+)[,][ ][y][ ][=][ ]([\\d]+)");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    x = Integer.parseInt(matcher.group(1));
                    y = Integer.parseInt(matcher.group(1));

                    board = new byte[x + 20][y + 20];

                    break;
                }
            } else if(line.charAt(0) == 'y'){
                Pattern pattern = Pattern.compile("[y][ ][=][ ]([\\d]+)[,][ ][x][ ][=][ ]([\\d]+)");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    x = Integer.parseInt(matcher.group(1));
                    y = Integer.parseInt(matcher.group(1));

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
                    if (matcher.group(2).matches("[$]")) {
                        patternRows++;
                        patternCols = 0;

                    } else if (matcher.group(2).matches("[BbOo]")) {
                        patternCols++;
                        if (matcher.group(2).matches("o")) {
                            board[patternRows][patternCols] = (byte) 1;
                        }
                        if (matcher.group(2).matches("b")) {
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
