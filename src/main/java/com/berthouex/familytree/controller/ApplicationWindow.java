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
    private Button saveNodeEditsButton;
    @FXML
    private Button closeTree;
    @FXML
    private  Label listSelectorLabel;
    @FXML
    private ListView<Graph> graphListView;

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
//        rightControlPanel.setMaxWidth(800);

        statusLabel.setWrapText(true);
        statusLabel.prefWidthProperty().bind(rightControlPanel.widthProperty());
        statusLabel.setText("Ready");

        this.openGraphs = FXCollections.observableArrayList();

        this.graphListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.graphListView.setItems(openGraphs);
        this.graphListView.setCellFactory(view -> new GraphCell());

        this.graphListView.getSelectionModel()
            .selectedItemProperty()
            .addListener((observable, oldGraph, newGraph) -> selectGraph(newGraph));
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
            graphListView.getSelectionModel().select(graph);
            statusLabel.setText("Created New Graph");
        });
    }

    /**
     * TODO add edit graph functionality
     * Opens a new <code>GraphModal</code> and creates a new <code>Graph</code>, then opens that graph.
     * @return
     */
    private Optional<Graph> showGraphDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationWindow.class.getResource("graph-modal.fxml"));
            Parent root = loader.load();
            GraphModal graphModal = loader.getController();

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
     * Opens the New Node modal and allows a user to create a new Node, which is then added to the currently open/selected <code>Graph</code>.
     * If somehow there is no open graph, this does nothing.
     */
    @FXML
    protected void createNewNode() {
        if (currentGraph != null) {
            Optional<GraphNode> result = showNodeDialog();

            result.ifPresent(node -> {
                currentGraph.addNode(node);
                node.setNodeOnMouseClicked(e -> selectNode(e));
                statusLabel.setText("Created New Node");
            });

            currentGraph.getNodes().stream()
                .filter(node -> !contentPane.getChildren().contains(node))
                .forEach(node -> contentPane.getChildren().add(node));
        } else {
            statusLabel.setText("No Tree Open");
        }
    }

    /**
     * Shows a modal to allow the creation of a new <code>GraphNode</code>.
     *
     * @return an <code>Optional</code> containing a <code>GraphNode</code>
     */
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

    ///////////////////////////////////////////////////////////////////////////
    // BASIC FUNCTION
    ///////////////////////////////////////////////////////////////////////////

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
     * Save and close an open Graph
     * TODO
     */
    public void closeGraph() {
        System.out.println("Close selected list item");

        Graph selected = graphListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            graphListView.getItems().remove(selected);
        }

        graphListView.getSelectionModel().select(null); // TODO investigate effects

        graphListView.getSelectionModel().clearSelection();
        contentPane.getChildren().clear();
        currentGraph.getNodes().stream()
            .filter(node -> !contentPane.getChildren().contains(node))
            .forEach(node -> contentPane.getChildren().add(node));
    }

    /**
     * Shows the system's <code>File Explorer</code> or equivalent and loads an existing save, if present.
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

    /**
     * @param file  a saved <code>Graph</code>
     */
    private void doOpen(File file) {
        for (Graph graph : openGraphs) {
            if (file.getAbsolutePath().equals(graph.getFilepath())) {
                graphListView.getSelectionModel().select(graph);
                return;
            }
        }

        try {
            Graph graph = DatabaseManager.load(file.getAbsolutePath());
            openGraphs.add(graph);
            graphListView.getSelectionModel().select(graph);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Edit an existing Node
     * TODO
     */
    @FXML
    protected void updateNode() {
        System.out.println("Edit Node - Saved");
    }

    /**
     * Save the current Tree
     * TODO
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
     * Save all open <code>Graphs</code> and Exits the application with {@link Platform#exit()}
     */
    @FXML
    protected void exitApplication() {
        doSave();
        Platform.exit();
    }


    ///////////////////////////////////////////////////////////////////////////
    // SELECT
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Populates the {@link #rightControlPanel} with information from the selected <code>Node</code>.
     *
     * @param node  the <code>GraphNode</code> to select
     */
    private void selectNode(GraphNode node) {
        clearNodeInfoPanel();

        boolean hasNode = (node != null);
        nodeInfoPanel.setVisible(hasNode);
        nodeInfoPanel.setManaged(hasNode);
        nodeInfoPanel.setDisable(!hasNode);

        if (node != null) {
            statusLabel.setText("Selected Node " + node.getFullName());

            nodeFirstName.setText(node.getFirstName());
            nodeLastName.setText(node.getLastName());

            if (node.hasBirthdate()) {
                nodeBirthDate.setValue(node.getBirthDate());
            }
            if (node.hasDeathDate()) {
                nodeDeathDate.setValue(node.getDeathDate());
            }
            if (node.hasBiography()) {
                nodeBiography.setText(node.getBiography());
            }
        }
    }

    private void clearNodeInfoPanel() {
        nodeFirstName.setText(null);
        nodeLastName.setText(null);
        nodeBirthDate.setValue(null);
        nodeDeathDate.setValue(null);
        nodeBiography.setText(null);
    }

    /**
     * Populates the {@link #contentPane} with data from the selected <code>Graph</code>.
     * @param graph the <code>Graph</code> to select
     */
    private void selectGraph(Graph graph) {
        boolean hasGraph = (graph != null) && (!graphListView.getItems().isEmpty());
        emptyState.setVisible(!hasGraph);
        emptyState.setManaged(!hasGraph);

        contentPane.setVisible(hasGraph);
        contentPane.setManaged(hasGraph);

        if (graph != null) {
            statusLabel.setText("Selected Graph " + graph.getGraphName());

            if (graph.hasNodes()) {
                contentPane.getChildren().clear();
                currentGraph.getNodes().stream()
                    .filter(node -> !contentPane.getChildren().contains(node))
                    .forEach(node -> contentPane.getChildren().add(node));
            }

            currentGraph = graph;
            graphListView.getSelectionModel().select(graph);
        }

        saveButton.setDisable(!hasGraph);
        leftControlPanel.setDisable(!hasGraph);
    }


    ///////////////////////////////////////////////////////////////////////////
    // MISC
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Returns the <code>Stage</code> this <code>ApplicationWindow</code> exists within.
     * Assumes there will always be a {@link #header} object to reference.
     *
     * @return  the root <code>Stage</code> object
     */
    private Stage getStage() {
        return (Stage) header.getScene().getWindow();
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

}
