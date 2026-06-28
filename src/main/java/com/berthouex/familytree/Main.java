package com.berthouex.familytree;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Initializes the application
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // TODO consider removing default window controls (close, minimize, maximize)

        // if the path of this class and the path to the fxml equivalent are the same, only the filename is required.
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("application-window.fxml"));

        // set default window size and maximize window (not full screen)
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setScene(scene);
        stage.setMaximized(true);

        // set icon, title
        stage.setTitle("Family Tree");
        try {
            Image icon = new Image(Main.class.getResourceAsStream("images/tree_64x.png"));
            stage.getIcons().add(icon);
        } catch (NullPointerException ignored) {

        }

        stage.show();
    }

}
