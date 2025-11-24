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

    private String name;
    private String quantity;

    @ManyToOne
    private Recipe recipe;

    public RecipeIngredients(Recipe recipe) {
        this.recipe = recipe;
        this.name = name;
        this.quantity = quantity;
    }

    public RecipeIngredients(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public RecipeIngredients(Long id, String name, String quantity) {
        this.recipeIngredientsId = id;
        this.name = name;
        this.quantity = quantity;
    }

    public RecipeIngredients() {
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof RecipeIngredients otherRecipeIngredients) {
            return name.equals(otherRecipeIngredients.getName()) && quantity.equals(otherRecipeIngredients.quantity);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
