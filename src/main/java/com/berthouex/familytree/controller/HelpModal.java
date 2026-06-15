package com.berthouex.familytree.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpModal implements Initializable {
    @FXML
    private Label helpPageTitle;
    @FXML
    private Button closeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    protected void close() {
        ((Stage) helpPageTitle.getScene().getWindow()).close();
    }

}
