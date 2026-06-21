package com.berthouex.familytree.model;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.UUID;

/**
 * A Family Tree containing members of the family and their relations to each other.
 */
public class Graph {
    private final String id;
    private final String graphName;
    private final ObservableList<GraphNode> graphNodes;
    private String filepath;

    public Graph(String graphName) {
        this.graphName = graphName;
        this.graphNodes = FXCollections.observableArrayList();
        this.id = UUID.randomUUID().toString();
    }

    public boolean addNode(GraphNode graphNode) {
        return this.graphNodes.add(graphNode);
    }

    public boolean hasNodes() {
        return !graphNodes.isEmpty();
    }

    public void addListener(Pane parent) {
        graphNodes.addListener((ListChangeListener<GraphNode>) change -> {
            try {
                if (change.wasRemoved()) {
                    parent.getChildren().removeAll(change.getRemoved());
                }

                if (change.wasAdded()) {
                    parent.getChildren().addAll(change.getAddedSubList());
                }
            } catch (IllegalStateException ignored) {

            }
        });
    }

    public List<GraphNode> getNodes() {
        return graphNodes;
    }

    public String getId() {
        return id;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getGraphName() {
        return graphName;
    }

    public Graph setFilepath(String filepath) {
        this.filepath = filepath;
        return this;
    }

    /**
     * Creates the connecting lines between Nodes on the Graph.
     */
    public void computeEdges() {

    }

}
