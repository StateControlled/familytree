package com.berthouex.familytree.controller;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

import com.berthouex.familytree.model.Graph;

/**
 * Represents a <code>Graph</code> in the selection list of the <code>ApplicationWindow</code>
 */
public class GraphCell extends ListCell<Graph> {

    public GraphCell() {
        super();
    }

    @Override
    protected void updateItem(Graph graph, boolean empty) {
        super.updateItem(graph, empty);

        if (empty || graph == null) {
            this.setText(null);
            this.setGraphic(null);
        } else {
            VBox vbox = new VBox(2);
            Label name = new Label(graph.getGraphName());
            vbox.getChildren().add(name);

            this.setGraphic(vbox);
            this.setText(null);
        }
    }

}
