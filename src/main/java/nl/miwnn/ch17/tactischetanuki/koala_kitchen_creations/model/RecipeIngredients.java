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
    private Long recipeIngredientsId;

    private String quantity;

    private String unit;

    @ManyToOne
    private Recipe recipe;

    @ManyToOne
    private Ingredient ingredient;

    public RecipeIngredients(Recipe recipe) {
        this.recipe = recipe;
    }

    public RecipeIngredients(Ingredient ingredient, String quantity, String unit) {
        this();
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
    }

    public RecipeIngredients(Long id, Ingredient ingredient, String quantity, String unit) {
        this(ingredient, quantity, unit);
        this.recipeIngredientsId = id;
    }

    public RecipeIngredients() {
    }

}
