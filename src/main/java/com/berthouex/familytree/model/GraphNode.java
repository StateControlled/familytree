package com.berthouex.familytree.model;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
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
    private static final Paint DEFAULT_COLOR = new Color(0.42, 0.49, 0.68, 0.85);
    private static final float DEFAULT_WIDTH = 96.0f;
    private static final float DEFAULT_HEIGHT = 128.f;

    private final String nodeId;
    private final List<GraphNode> parentNodes;
    private final List<GraphNode> childNodes;

    private Text headerLabel;
    private Rectangle rectangle;

    private String firstName;
    private String lastName;
    private String biography;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private int age;

    private double x;
    private double y;

    private GraphNode(Builder builder) {
        this(builder.firstName, builder.lastName, builder.biography, builder.birthDate, builder.deathDate);
    }

    private GraphNode(String firstName, String lastName, String biography, LocalDate birthDate, LocalDate deathDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.biography = biography;
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

        headerLabel = new Text(firstName + " " + lastName);
        headerLabel.setFont(new Font("Courier", 16));
        headerLabel.setFill(Color.BLACK);

        rectangle = new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_COLOR);
        setBoxHeight();

        this.getChildren().addAll(rectangle, headerLabel);

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

    /**
     * Sets the height of the Node border box to an appropriate height
     */
    private void setBoxHeight() {
        // height of header + birthdate + deathdate + bio + age
    }

    /**
     * Sets the background color of this <code>GraphNode</code>. If the argument is null, sets the color to the
     * default color option.
     *
     * @param color the new background color
     */
    public void setColor(Paint color) {
        if (color == null) {
            this.rectangle.setFill(DEFAULT_COLOR);
        } else {
            this.rectangle.setFill(color);
        }
    }

    /**
     *
     * @param text  a <code>String</code> that will be set as the header text.
     *
     * @throws IllegalArgumentException if argument <code>text</code> is null
     */
    public void setHeader(String text) throws IllegalArgumentException {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null");
        }
        headerLabel = new Text(text);

        double width = headerLabel.getLayoutBounds().getWidth();
        width += 8;
        this.rectangle.setWidth(width);
    }


    /**
     * A builder of <code>GraphNodes</code>. The builder configures the first and last names, birth and death dates, and biography.
     */
    public static class Builder {
        private String firstName;
        private String lastName;
        private String biography;
        private LocalDate birthDate;
        private LocalDate deathDate;

        public Builder() {

        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder description(String description) {
            this.biography = description;
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder deathDate(LocalDate deathDate) {
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

    /**
     * Attempts to compute the age of this family member.
     * If both a birthdate and death date are provided, set <code>age</code> to the period between.
     * If a birthdate is given but a death date is not, compute age as the period between the birthdate and today.
     */
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
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return  the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName  a last name or family name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        return -1;
    }

    /**
     * @param ageInYears    the age in years
     */
    public void setAge(int ageInYears) {
        this.age = ageInYears;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    /**
     * When a death date is entered, try to compute the age of the subject. If a birthdate has not been entered,
     * only the death date is set.
     *
     * @param deathDate the date of death
     */
    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
        tryComputeAge();
    }

    public boolean isDeceased() {
        return this.deathDate != null;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * @param birthDate the date of birth
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBiography() {
        return biography;
    }

    /**
     * @param biography   a biography to attach to this Node
     */
    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setPosition(float x, float y) {
        setXPos(x);
        setYPos(y);
    }

    public void setXPos(float x) {
        this.x = x;
        this.setLayoutX(x);
    }

    public void setYPos(float y) {
        this.y = y;
        this.setLayoutY(y);
    }

    /**
     * Returns a new object with the same data from the original object.
     *
     * @param original  the <code>GraphNode</code> to copy
     * @return          a new <code>GraphNode</code> object
     */
    public static GraphNode copyOf(GraphNode original) {
        String firstName = original.firstName;
        String lastName = original.lastName;
        LocalDate bDate = original.birthDate;
        LocalDate dDate = original.deathDate;
        String description = original.biography;

        return new GraphNode(firstName, lastName, description, bDate, dDate);
    }

    @Override
    public String toString() {
        return "GraphNode {" +
            "nodeId='" + nodeId + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", biography='" + biography + '\'' +
            ", birthDate=" + birthDate +
            ", deathDate=" + deathDate +
            ", age=" + age +
            ", x=" + x +
            ", y=" + y +
            ", parentNodes=" + parentNodes +
            ", childNodes=" + childNodes +
            '}';
    }

}
