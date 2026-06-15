package com.berthouex.familytree.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A Family Tree containing members of the family and their relations to each other.
 */
public class Graph {
    private final String id;
    private final String graphName;
    private final List<GraphNode> graphNodes;
    private String filepath;

    public Graph(String graphName) {
        this.graphName = graphName;
        this.graphNodes = new ArrayList<>();
        this.id = UUID.randomUUID().toString();
    }

    public boolean addNode(GraphNode graphNode) {
        return this.graphNodes.add(graphNode);
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
