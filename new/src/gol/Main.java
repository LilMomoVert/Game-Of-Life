package gol;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Created by Momcilo Delic on 2/10/2017.
 */


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View/gol.fxml"));
        Parent root = loader.load();
        primaryStage.getIcons().add(new Image("img/monster.png"));
        primaryStage.setTitle("  Game Of Life - GoL");
        primaryStage.setScene(new Scene(root, 1220, 835));
        primaryStage.setResizable(false);
        primaryStage.show();

    }

}