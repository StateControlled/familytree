package com.berthouex.familytree.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.berthouex.familytree.model.Graph;

/**
 * Temporary window to start a new Graph
 */
public class GraphModal implements Initializable {
    @FXML
    private Button cancelButton;
    @FXML
    private Button createButton;
    @FXML
    private Label graphDialogTitle;
    @FXML
    private TextField nameField;

//    private Graph editing;
    private Graph result;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

//    public void init(Graph graph) {
//        if (graph != null) {
//            this.editing = graph;
//            nameField.setText(graph.getGraphName());
//        }
//    }

    public void createGraph() {
        System.out.println("Create new");
        String text = nameField.getText().trim();

        if (text.isEmpty()) {
            return;
        }
        System.out.println(text);
        result = new Graph(text);

        close();
    }

    public Optional<Graph> getResult() {
        if (result == null) {
            return Optional.empty();
        }
        return Optional.of(result);
    }

    @FXML
    protected void cancel() {
        close();
    }

    protected void close() {
        ((Stage) graphDialogTitle.getScene().getWindow()).close();
    }

}
