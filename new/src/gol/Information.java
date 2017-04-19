package gol;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

/**
 * Created by Kani Boyka on 3/29/2017.
 */
public class Information {

    /**
     * This class is for different type of information.
     * @author Momcilo Delic - s315282
      */
    public void aboutGOL(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game of Life");
        alert.setHeaderText("Om Game of Life");
        alert.setContentText("The Game of Life er ikke et vanlig dataspill. Det er en 'cellulær automat', og ble oppfunnet av Cambridge matematikeren John Conway. \n \nDette spillet ble kjent da det ble nevnt i en artikkel publisert av Scientific American i 1970. Den består av en samling av celler som, basert på noen få matematiske regler, den kan leve, dø eller formere seg. Avhengig av startbetingelsene , cellene kan danne ulike mønstre gjennom hele spillet.");
        alert.getDialogPane().setPrefSize(720, 320);

        alert.showAndWait();
    }

    public void authorInfo(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Made by");
        alert.setHeaderText("Programmet er laget av:");
        alert.setContentText(" \n Momcilo Delic - s315282 ");
        alert.getDialogPane().setPrefSize(320, 220);
        alert.showAndWait();
    }

    public void AOBCanvas(){
        Alert out = new Alert(Alert.AlertType.INFORMATION);
        out.setTitle("Info");
        out.setHeaderText("Out of Bounds");
        out.setContentText(" \n Du tegner utenfor spillebrette. ");
        out.getDialogPane().setPrefSize(320, 220);
        out.showAndWait();
    }

}
