package com.berthouex.familytree.model;

import java.time.LocalDate;

import com.berthouex.familytree.controller.ApplicationWindow;

/**
 * A read-only data transfer object to facilitate communicating information from a {@link GraphNode} to the {@link ApplicationWindow} GUI.
 */
public record NodeData(String nodeId, String firstName, String lastName, String biography, LocalDate birthDate, LocalDate deathDate, int age) {
    /**
     * Returns the first and last names concatenated, separated by a space.
     *
     * @return  the full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * @return  <code>true</code> if the birthdate is not null
     */
    public boolean hasBirthdate() {
        return this.birthDate != null;
    }

    /**
     * @return  <code>true</code> if the death date is not null
     */
    public boolean hasDeathDate() {
        return this.deathDate != null;
    }

    /**
     * @return  <code>true</code> if the biography is not null
     */
    public boolean hasBiography() {
        return this.biography != null;
    }
}
