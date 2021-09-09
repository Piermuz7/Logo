package it.cs.unicam.pa2021.logo;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/jlogo.fxml"));
        stage.getIcons().add(new Image(getClass().getResource("/logoIcon.jpg").toString()));
        stage.setTitle("JLOGO");
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();
    }
}
