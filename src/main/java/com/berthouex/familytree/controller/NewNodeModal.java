package com.berthouex.familytree.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.controlsfx.control.ToggleSwitch;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import com.berthouex.familytree.model.GraphNode;

public class NewNodeModal implements Initializable {

    @FXML
    private Button cancelButton;
    @FXML
    private Button createButton;
    @FXML
    private Label nodeDialogTitle;

    @FXML
    private ToggleSwitch toggleSwitch;

    @FXML
    private Label deathDateLabel;

    @FXML
    private TextField firstNameText;
    @FXML
    private TextField lastNameText;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private DatePicker deathDatePicker;
    @FXML
    private TextArea descriptionText;

    private GraphNode result;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
            deathDatePicker.setVisible(newValue);
            deathDateLabel.setVisible(newValue);
        });
    }

    public void createNode() {
        String firstName = this.firstNameText.getText().trim();
        String lastName = this.lastNameText.getText().trim();
        LocalDate bDate = this.birthDatePicker.getValue();
        LocalDate dDate = this.deathDatePicker.getValue();
        String description = this.descriptionText.getText().trim();

        GraphNode.Builder builder = new GraphNode.Builder();

        if (!firstName.isEmpty()) {
            builder.firstName(firstName);
        }
        if (!lastName.isEmpty()) {
            builder.lastName(lastName);
        }
        if (bDate != null) {
            builder.birthDate(bDate);
        }
        if (dDate != null) {
            builder.deathDate(dDate);
        }
        if (!description.isEmpty()) {
            builder.description(description);
        }

        result = builder.build();
        System.out.println(result);

        close();
    }

    public Optional<GraphNode> getResult() {
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
        ((Stage) nodeDialogTitle.getScene().getWindow()).close();
    }

}
