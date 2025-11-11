package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 *
 */
@Getter
@Setter
@Entity
public class RecipeIngredients {

    @Id
    @GeneratedValue
    private Long RecipeIngredientsId;

    private String name;
    private String quantity;

    @ManyToOne
    private Recipe recipe;

    public RecipeIngredients(Recipe recipe) {
        this.recipe = recipe;
        this.name = name;
        this.quantity = quantity;
    }

    public RecipeIngredients() {
    }
}
