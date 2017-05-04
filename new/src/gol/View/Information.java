package gol.View;

import gol.Model.Board.DynamicBoard;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

/**
 * Created by Kani Boyka on 3/29/2017.
 */
public class Information {


    /**
     * This class is for different type of information.
     * @author Momcilo Delic - s315282
      */

    public void instructions(){

        String gameinstructions =
                "\n\n- Du kan tegne på brettet ved hjelp av venstre mus.\n\n" +
                "- For å fjerne celler må du holde inne CTRL + Mousedrag/MouseClick\n\n" +
                "- Play knappen får spillet til å kjøre, Stop stopper spillet (Genius) \n\n" +
                "- Du kan skifte fargen på cellene, gridden og bakgrunnen ved hjelp av colorpicker\n\n"+
                "- Bruk slidere for å justere på hastigheten og størelsen\n\n" +
                        "- Clear funksjonen clearer brettet og Randomize generer randome celler\n\n" +
                        "- Bruk pilene for å flytte på møsteret hvor du vil på brettet\n(Denne funksjonen kan være nyttig for å flytte mønsteret du har lastet midt på brettet)\n\n";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Hvordan spillet fungerer:");
        alert.setContentText(gameinstructions);
        alert.getDialogPane().setPrefSize(600, 600);

        alert.showAndWait();

    }

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

    public void ErrorStaticBoard(){
        Alert errorStatic = new Alert(Alert.AlertType.INFORMATION);
        errorStatic.setTitle("Info");
        errorStatic.setHeaderText("Game Information");
        errorStatic.setContentText(" \nDu kan ikke flytte på mønsteret når brettet er satt på 'STATIC'");
        errorStatic.getDialogPane().setPrefSize(320, 300);
        errorStatic.showAndWait();
    }

    public void Ops(){
        Alert out = new Alert(Alert.AlertType.INFORMATION);
        out.setTitle("ERROR");
        out.setHeaderText("Feil");
        out.setContentText("Det har skjedd noe feil!");
        out.getDialogPane().setPrefSize(320, 220);
        out.showAndWait();
    }

    public void Error(){
        Alert out = new Alert(Alert.AlertType.INFORMATION);
        out.setTitle("Info");
        out.setHeaderText("Feil, les meldingen under!");
        out.setContentText("Du kan ikke justere på size Slideren når 'Dynamic Size' er på. Slå av 'Dynamic Size' for å bruke slideren. ");
        out.getDialogPane().setPrefSize(320, 220);
        out.showAndWait();
    }

}
