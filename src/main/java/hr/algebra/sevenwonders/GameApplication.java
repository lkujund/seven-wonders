package hr.algebra.sevenwonders;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameApplication extends Application {

    private static Scene mainScene;


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("boardScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1440, 900);
        stage.setTitle("Seven Wonders - Luka Kujundzic");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        mainScene = scene;
    }

    public static void main(String[] args) {
        launch();
    }
}