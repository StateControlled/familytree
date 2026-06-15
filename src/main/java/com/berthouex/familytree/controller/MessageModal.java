package com.berthouex.familytree.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MessageModal implements Initializable {
    @FXML
    private Label messageTitle;
    @FXML
    private TextArea messagePrimary;
    @FXML
    private TextArea messageSecondary;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    protected void clickOk() {
        close();
    }

    @FXML
    protected void clickCancel() {
        close();
    }

    private void close() {
        ((Stage) okButton.getScene().getWindow()).close();
    }

}
