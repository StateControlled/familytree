package com.berthouex.familytree.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.berthouex.familytree.data.DatabaseManager;
import com.berthouex.familytree.model.Graph;
import com.berthouex.familytree.model.GraphNode;

/**
 * The primary window the user will interact with.
 */
@SuppressWarnings("unused")
public class ApplicationWindow implements Initializable {
    // Right pane
    @FXML
    private Label statusLabel;

    // Center
    @FXML
    private VBox emptyState;
    @FXML
    private Pane contentPane;

    // Left pane controls
    @FXML
    private VBox leftControlPanel;
    @FXML
    private Label leftLabel;
    @FXML
    private Button createNewNode;
    @FXML
    private Button editNode;
    @FXML
    private Button closeTree;
    @FXML
    private  Label listSelectorLabel;
    @FXML
    private ListView<Graph> listSelector;

    // Top row controls
    @FXML
    private VBox header;
    @FXML
    private Button openButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button newGraphButton;
    @FXML
    private Button helpButton;
    @FXML
    private Button exitButton;

    private ObservableList<Graph> openGraphs;

    private Graph currentGraph;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.openGraphs = FXCollections.observableArrayList();
        this.listSelector.setItems(openGraphs);
        this.listSelector.setCellFactory(view -> new GraphCell());
        this.listSelector.getSelectionModel()
            .selectedItemProperty()
            .addListener((observable, oldGraph, newGraph) -> selectGraph(newGraph));
    }

    private void selectGraph(Graph graph) {
        boolean hasGraph = (graph != null);
        emptyState.setVisible(!hasGraph);
        emptyState.setManaged(!hasGraph);

        contentPane.setManaged(hasGraph);
        saveButton.setDisable(!hasGraph);
        leftControlPanel.setDisable(!hasGraph);
    }

    /**
     * Creates a new Tree
     */
    @FXML
    protected void createNewGraph() {
        Optional<Graph> result = showGraphDialog();

        result.ifPresent(graph -> {
            openGraphs.add(graph);
            listSelector.getSelectionModel().select(graph);
            statusLabel.setText("Created New Graph");
        });
    }

    private Optional<Graph> showGraphDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationWindow.class.getResource("graph-modal.fxml"));
            Parent root = loader.load();
            GraphModal graphModal = loader.getController();
//            graphModal.init(graph);

            Stage stage = buildModal("Tree", root);
            stage.showAndWait();

            return graphModal.getResult();

        } catch (IOException e) {
            System.out.println("Failed to create a new dialog box: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }

    }

    private Stage buildModal(String title, Parent root) {
        Stage dialog = new Stage();
        dialog.initOwner(getStage());
        dialog.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(root);
        dialog.setScene(scene);
        dialog.setResizable(false);
        dialog.setTitle(title);

        return dialog;
    }

    /**
     * Open an existing save file
     */
    @FXML
    protected void open() {
        FileChooser fileChooser = buildFileChooser("Select Saved Tree");
        File file = fileChooser.showOpenDialog(this.getStage());

        if (file != null) {
            doOpen(file);
        }
    }

    private void doOpen(File file) {
        for (Graph graph : openGraphs) {
            if (file.getAbsolutePath().equals(graph.getFilepath())) {
                listSelector.getSelectionModel().select(graph);
                return;
            }
        }

        try {
            Graph graph = DatabaseManager.load(file.getAbsolutePath());
            openGraphs.add(graph);
            listSelector.getSelectionModel().select(graph);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Creates a <code>FileChooser</code> object for browsing local files.
     *
     * @param title the window title
     * @return  a <code>FileChooser</code> object
     */
    private FileChooser buildFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tree (*.json)", "*.json"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        return fileChooser;
    }

    /**
     * Save the current Tree
     */
    @FXML
    protected void save() {
//        FileChooser fileChooser = buildFileChooser("Save Current Tree");
        doSave();
    }

    private void doSave() {
        System.out.println("Save");
    }

    @FXML
    protected void showHelp() {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationWindow.class.getResource("help-modal.fxml"));
            Parent root = loader.load();

            Stage stage = buildModal("Help", root);
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println("Failed to create a new dialog box: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Create a new Node
     */
    @FXML
    protected void createNewNode() {
        Optional<GraphNode> result = showNodeDialog();

        result.ifPresent(node -> {
            currentGraph.addNode(node);
            statusLabel.setText("Created New Node");
        });
    }

    private Optional<GraphNode> showNodeDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationWindow.class.getResource("new-node-modal.fxml"));
            Parent root = loader.load();
            NewNodeModal nodeModal = loader.getController();

            Stage stage = buildModal("New Node", root);
            stage.showAndWait();

            return nodeModal.getResult();

        } catch (IOException e) {
            System.out.println("Failed to create a new dialog box: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }

    }

    /**
     * Edit an existing Node
     */
    @FXML
    protected void editNode() {
        System.out.println("Edit Node");
    }

    /**
     * Save and close an open Graph
     */
    public void closeGraph() {
        System.out.println("Close List Item");
    }

    /**
     * Saves and Exits the application
     */
    @FXML
    protected void exitApplication() {
        doSave();
        Platform.exit();
    }

    private Stage getStage() {
        return (Stage) header.getScene().getWindow();
    }

    public Pane getContentPane() {
        return contentPane;
    }

    /**
     * @return  the text of the status label
     */
    public String getStatusLabel() {
        return statusLabel.getText();
    }

    public Label getStatusLabelObject() {
        return statusLabel;
    }

}
