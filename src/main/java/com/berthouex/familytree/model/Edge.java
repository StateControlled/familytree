package com.berthouex.familytree.model;

import javafx.scene.Group;
import javafx.scene.shape.Line;

public class Edge extends Group {

    public Edge(GraphNode source, GraphNode target) {
        Line line = new Line();
        this.getChildren().add(line);

        // Bind edge line coordinates to center properties of the vertex StackPanes
        line.startXProperty().bind(
            source.layoutXProperty().add(source.widthProperty().divide(2))
        );

        line.startYProperty().bind(
            source.layoutYProperty().add(source.heightProperty().divide(2))
        );

        line.endXProperty().bind(
            target.layoutXProperty().add(target.widthProperty().divide(2))
        );

        line.endYProperty().bind(
            target.layoutYProperty().add(target.heightProperty().divide(2))
        );
    }

}
