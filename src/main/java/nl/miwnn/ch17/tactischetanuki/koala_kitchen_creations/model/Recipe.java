package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredients> recipeIngredients;

    @Column(columnDefinition = "TEXT")
    String description;

    public Recipe(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Recipe() {
    }
}
