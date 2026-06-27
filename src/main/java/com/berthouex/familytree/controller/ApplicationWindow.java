package com.berthouex.familytree.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
//@SuppressWarnings("unused")
public class ApplicationWindow implements Initializable {
    // Right pane
    @FXML
    private VBox rightControlPanel;
    @FXML
    private TextField nodeFirstName;
    @FXML
    private TextField nodeLastName;
    @FXML
    private DatePicker nodeBirthDate;
    @FXML
    private DatePicker nodeDeathDate;
    @FXML
    private TextArea nodeBiography;
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
    private VBox nodeInfoPanel;
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

    // Other
    private ObservableList<Graph> openGraphs;

    private Graph currentGraph;
    private GraphNode currentNode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rightControlPanel.setPrefWidth(160);
        rightControlPanel.setMinWidth(100);
        rightControlPanel.setMaxWidth(800);
        statusLabel.setWrapText(true);
        statusLabel.prefWidthProperty().bind(rightControlPanel.widthProperty());
        statusLabel.setText("Ready");

        this.openGraphs = FXCollections.observableArrayList();

        this.listSelector.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.listSelector.setItems(openGraphs);
        this.listSelector.setCellFactory(view -> new GraphCell());

        this.listSelector.getSelectionModel()
            .selectedItemProperty()
            .addListener((observable, oldGraph, newGraph) -> selectGraph(newGraph));

//        this.listSelector.setOnMouseClicked(event -> {
//            if (listSelector.getSelectionModel().getSelectedItem() != null) {
//                listSelector.getSelectionModel().clearSelection();
//            }
//        });

        ChangeListener<GraphNode> cn = new ChangeListener<GraphNode>() {
            @Override
            public void changed(ObservableValue<? extends GraphNode> observable, GraphNode oldValue, GraphNode newValue) {
                selectNode(newValue);
            }
        };

    }

    private void selectNode(GraphNode node) {
        boolean hasNode = (node != null);
        nodeInfoPanel.setVisible(hasNode);
        nodeInfoPanel.setManaged(hasNode);
        nodeInfoPanel.setDisable(!hasNode);
    }

    private void selectGraph(Graph graph) {
        boolean hasGraph = (graph != null);
        emptyState.setVisible(!hasGraph);
        emptyState.setManaged(!hasGraph);

        contentPane.setVisible(hasGraph);
        contentPane.setManaged(hasGraph);

        if (graph != null) {
            statusLabel.setText("Selected Graph " + graph.getGraphName());

            if (graph.hasNodes()) {
                clearAndUpdateContentPane();
            }
            currentGraph = graph;
            listSelector.getSelectionModel().select(graph);
        }

        saveButton.setDisable(!hasGraph);
        leftControlPanel.setDisable(!hasGraph);
    }

    /**
     * Clears the ContentPane and adds all the <code>GraphNodes</code> from the current Graph.
     * Does not check to see that {@link #currentGraph} is not <code>null</code> or that the {@link #currentGraph} has Nodes.
     */
    public void clearAndUpdateContentPane() {
        contentPane.getChildren().clear();
        updateContentPane();
    }

    /**
     * Adds any <code>GraphNodes</code> that are not present on the ContentPane to the Pane.
     * Does not check to see that {@link #currentGraph} is not <code>null</code> or that the {@link #currentGraph} has Nodes.
     */
    public void updateContentPane() {
        currentGraph.getNodes().stream()
            .filter(node -> !contentPane.getChildren().contains(node))
            .forEach(node -> contentPane.getChildren().add(node));
    }

    /**
     * Creates a new Tree.
     * Opens a new <code>GraphModal</code> that allows the user to create a new <code>Graph</code>.
     * If the user creates a new <code>Graph</code>, the Graph will be added to the left sidebar.
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

    /**
     * Create a new Node
     */
    @FXML
    protected void createNewNode() {
        if (currentGraph != null) {
            Optional<GraphNode> result = showNodeDialog();

            result.ifPresent(node -> {
                currentGraph.addNode(node);
                statusLabel.setText("Created New Node");
            });
            updateContentPane();
        }
    }

    private Optional<GraphNode> showNodeDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationWindow.class.getResource("new-node-modal.fxml"));
            Parent root = loader.load();
            NewNodeModal nodeModal = loader.getController();

            Stage stage = buildModal("Create New Node", root);
            stage.showAndWait();

            return nodeModal.getResult();
        } catch (IOException e) {
            System.out.println("Failed to create a new dialog box: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }

    }

    /**
     * Clear list selection
     */
    private void clearSelection() {
        listSelector.getSelectionModel().select(null);
    }

    /**
     * Creates the basic modal window.
     *
     * @param title the window title
     * @param root  the element the modal will be a child to
     *
     * @return  a <code>Stage</code> object, the modal
     */
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
     * TODO implement functionality
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
     * TODO implement
     */
    @FXML
    protected void save() {
//        FileChooser fileChooser = buildFileChooser("Save Current Tree");
        doSave();
    }

    /**
     * TODO
     */
    private void doSave() {
        System.out.println("Save");
    }

    /**
     * Creates and shows a "help" window.
     * TODO add help information
     */
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
     * Edit an existing Node
     * TODO
     */
    @FXML
    protected void editNode() {
        System.out.println("Edit Node");
    }

    /**
     * Save and close an open Graph
     * TODO
     */
    public void closeGraph() {
        System.out.println("Close List Item");
        listSelector.getSelectionModel().clearSelection();
    }

    /**
     * Saves and Exits the application
     */
    @FXML
    protected void exitApplication() {
        doSave();
        Platform.exit();
    }

    /**
     * @return  the root <code>Stage</code> object
     */
    private Stage getStage() {
        return (Stage) header.getScene().getWindow();
    }

    /**
     * Returns the <code>Pane</code> object that is at the center of the application's main window.
     * @return  the {@link #contentPane}
     */
    public Pane getContentPane() {
        return contentPane;
    }

}
