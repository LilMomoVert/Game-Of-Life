package gol.Model.FileManager.Decoders;

import javafx.scene.control.Alert;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kani Boyka on 4/28/2017.
 */
public class RLEDecoder {

    private byte[][] boardTufte; //Skal sendes til gameboard på slutten av metuden under. lar oss ha costum størrelse.
    private int row = 0;
    private int col = 0;

    //henter data fra boardTufte som henter størrelse om brettet
    public byte[][] getBoard() {
        return boardTufte;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return col;
    }

    /**
     * readGameBoard gathers files and draws the actual grid.
     *
     * @author Andreas Jacobsen.
     * @version 1.0 - May 05, 2016
     *
     * @param r holds the information from the BufferedReader.
     * @throws IOException  Signals that an I/O exception of some sort has occurred.
     *
     * @see BufferedReader Reads text from a character-input stream, buffering characters so as to
     * provide for the efficient reading of characters, arrays, and lines.
     */
    public void readGameBoard(BufferedReader r) throws IOException { //split metoden, en for meta, en for celle og en for innhold.
        //Skal kunne lese metadata og brett
        String author = new String();
        String name = new String();
        String comment = new String();
        String line = new String();
        int colms;
        int rows;
        int number;

        StringBuilder melding = new StringBuilder();

        //WHILE LOOP FOR METAtestDATA OG BRETTSTØRRELSE
        while ((line = r.readLine()) != null) { //r er en buffered reader, sjekker på om det eksistere noe i BufferedReaderen.
            if (line.charAt(0) == '#') { //leser etter metadata i kommentarer her, åpner det og lagrer dataen om feks forfatter.
                if (line.charAt(1) == 'C') {
                    comment = line.substring(3);
                    System.out.println(comment);
                } else if (line.charAt(1) == 'N') {
                    name = line.substring(3);
                    System.out.println(name);
                } else if (line.charAt(1) == 'O') {
                    author = line.substring(3);
                    System.out.println(author);
                } else if (line.charAt(1) == 'c') { //dette kan også være comment, sjekk i RLE filer og på life-wiki..
                    comment = line.substring(3);
                    System.out.println(comment);
                }
                continue;

            } else if (line.charAt(0) == 'x') {
                Pattern bokstaver = Pattern.compile("[x][ ][=][ ]([\\d]+)[,][ ][y][ ][=][ ]([\\d]+)"); //her bruker vi groups så sidte \\d tester på tall, vi kan kalle dem med tallposisjoner
                Matcher matcher = bokstaver.matcher(line);
                if (matcher.find()) { //returnerer en bolsk verdi
                    rows = Integer.parseInt(matcher.group(2)); //for å gjøre det om til tall istedenfor string på rows (x-akse)
                    colms = Integer.parseInt(matcher.group(1)); //for å gjøre det om til tall istedenfor string på colmns (y-akse)

                    boardTufte = new byte[rows][colms];

                    break;
                    //Nå vil du være på første linje som omhandler brettet
                }
            }
        }


        Pattern thePattern = Pattern.compile("([0-9]*)([oObB$])");
        row = 0;
        col = 0;
        //while loop for the board
        while ((line = r.readLine()) != null) {
            Matcher patternMatch = thePattern.matcher(line);
            while (patternMatch.find()) {
                number = 1;
                if (patternMatch.group(1).matches("\\d+")) {
                    number = Integer.parseInt(patternMatch.group(1));
                }

                while (number-- > 0) {
                    if (patternMatch.group(2).matches("[BbOo]")) { //hvis pattern match er levende (0(
                        boardTufte[row][col++] = patternMatch.group(2).matches("o") ? (byte) 1 : 0; //så sett patternet til true, startposision 00.
                    } else if (patternMatch.group(2).matches("[$]")) {
                        row++;
                        col = 0;
                        continue;
                    }
                }

            }
        }
    }

    /**
     * readGameBoardFromDisk reades a file from disk onto board.
     *
     * @author Boris Illievski.
     * @version 1.0 - May 05, 20016.
     *
     * @param file file is the file which is uploaded.
     * @throws IOException IOException throws out an error message if needed.
     * @see File File package defines interfaces and classes for the Java virtual machine to access
     * files, file attributes, and file systems.
     */
    public void readGameBoardFromDisk(File file) throws IOException {
        if (!file.canRead()) {
            System.out.println("Kunne ikke lese fil.");
        }
        try (BufferedReader bfr = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            System.out.println("----!!!----");
            readGameBoard(bfr);
        } catch (FileNotFoundException FileNotFound) {
            System.out.println("Feilmelding for buffer");
        } catch (IOException IOEFeil) {
            System.out.println("Feilmelding for IOE-Feil");
        }
    }
}
