package com.berthouex.familytree.model;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a Node on a Graph, or a person on a family tree.
 *
 * @see Graph
 */
public class GraphNode extends StackPane {
    private final String nodeId;
    private final List<GraphNode> parentNodes;
    private final List<GraphNode> childNodes;

    private String firstName;
    private String lastName;
    private String description;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private int age;

    private double x;
    private double y;

    private GraphNode(Builder builder) {
        this(builder.firstName, builder.lastName, builder.description, builder.birthDate, builder.deathDate);
    }

    private GraphNode(String firstName, String lastName, String description, LocalDate birthDate, LocalDate deathDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.birthDate = birthDate;
        this.deathDate = deathDate;

        this.x = 0;
        this.y = 0;
        this.parentNodes = new ArrayList<>();
        this.childNodes = new ArrayList<>();
        this.nodeId = UUID.randomUUID().toString();

        init();
    }

    /**
     * Does other initialization work.
     */
    private void init() {
        setId("GraphNode");
        tryComputeAge();

        Text text = new Text(firstName + " " + lastName);
        Rectangle rectangle = new Rectangle(48, 64, Color.RED);
        this.getChildren().addAll(text, rectangle);

        this.setLayoutX(x);
        this.setLayoutY(y);

        // Allow node to be moved
        this.setOnMousePressed(e -> {
            x = e.getX();
            y = e.getY();
        });

        this.setOnMouseDragged(e -> {
            this.setLayoutX(this.getLayoutX() + e.getX() - x);
            this.setLayoutY(this.getLayoutY() + e.getY() - y);
        });
    }

    public static class Builder {
        private String firstName;
        private String lastName;
        private String description;
        private LocalDate birthDate;
        private LocalDate deathDate;

        public Builder() {

        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder setDeathDate(LocalDate deathDate) {
            this.deathDate = deathDate;
            return this;
        }

        public GraphNode build() {
            return new GraphNode(this);
        }

    }

    public List<GraphNode> getParentNodes() {
        return parentNodes;
    }

    public boolean addParentNode(GraphNode parent) {
        return parentNodes.add(parent);
    }

    public List<GraphNode> getChildNodes() {
        return childNodes;
    }

    public boolean addChildNode(GraphNode child) {
        return childNodes.add(child);
    }

    private void tryComputeAge() {
        if (birthDate != null && deathDate == null) {
            LocalDate temp = LocalDate.now();
            this.age = Period.between(birthDate, temp).getYears();
        }

        if (birthDate != null && deathDate != null) {
            this.age = Period.between(birthDate, deathDate).getYears();
        }

    }

    ////////////////////////////////////////////////////////////////
    // GETTERS AND SETTERS
    ////////////////////////////////////////////////////////////////

    /**
     * @return  the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName a first name
     * @return  a reference to this object
     */
    public GraphNode setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    /**
     * @return  the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName  a last name or family name
     * @return  a reference to this object
     */
    public GraphNode setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    /**
     * @return  the age in years
     */
    public int getAge() {
        return age;
    }

    public int getAgeInMonths() {
        if (birthDate != null && deathDate != null) {
            return Period.between(birthDate, deathDate).getMonths();
        }
        return 0;
    }

    /**
     * @param ageInYears    the age in years
     * @return  a reference to this object
     */
    public GraphNode setAge(int ageInYears) {
        this.age = ageInYears;
        return this;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    /**
     * When a death date is entered, try to compute the age of the subject. If a birthdate has not been entered,
     * only the death date is set.
     *
     * @param deathDate the date of death
     * @return  a reference to this object
     */
    public GraphNode setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
        tryComputeAge();
        return this;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * @param birthDate the date of birth
     * @return  a reference to this object
     */
    public GraphNode setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public String getDescription() {
        return description;
    }

    /**
     * @param description   a description to attach to this Node
     * @return  a reference to this object
     */
    public GraphNode setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getNodeId() {
        return nodeId;
    }

    public GraphNode setXPos(float x) {
        this.x = x;
        return this;
    }

    public GraphNode setYPos(float y) {
        this.y = y;
        return this;
    }

    /**
     * Returns a new object with the same data from the original object.
     *
     * @param original  the <code>GraphNode</code> to copy
     * @return  a new <code>GraphNode</code> object
     */
    public static GraphNode copyOf(GraphNode original) {
        String firstName = original.firstName;
        String lastName = original.lastName;
        LocalDate bDate = original.birthDate;
        LocalDate dDate = original.deathDate;
        String description = original.description;

        return new GraphNode(firstName, lastName, description, bDate, dDate);
    }

    @Override
    public String toString() {
        return "GraphNode{" +
            "nodeId='" + nodeId + '\'' +
            ", parentNodes=" + parentNodes +
            ", childNodes=" + childNodes +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", description='" + description + '\'' +
            ", birthDate=" + birthDate +
            ", deathDate=" + deathDate +
            ", age=" + age +
            ", x=" + x +
            ", y=" + y +
            '}';
    }

    public String getName() {
        return String.format("%s %s", firstName, lastName);
    }

}
