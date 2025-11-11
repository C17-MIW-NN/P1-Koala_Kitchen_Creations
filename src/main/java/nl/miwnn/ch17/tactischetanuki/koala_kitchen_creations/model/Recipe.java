package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 * Concept of a recipe
 */
@Getter
@Setter
@Entity
public class Recipe {

    @Id
    @GeneratedValue
    Long recipeId;

    String name;

    @Column(columnDefinition = "TEXT")
    String description;

    public Recipe(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Recipe() {
    }
}
