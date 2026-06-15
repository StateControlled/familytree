package com.berthouex.familytree;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Initializes the application
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // if the path of this class and the path to the fxml equivalent are the same, only the filename is required.
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("application-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1600, 900);

        // TODO consider removing default window controls (close, minimize, maximize)
        stage.setTitle("Family Tree");
        stage.setScene(scene);
        stage.show();
    }

}
