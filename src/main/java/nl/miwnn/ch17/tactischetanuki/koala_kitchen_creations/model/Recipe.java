package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredients> recipeIngredients;

    @ManyToMany
    private Set<Category> categories;

    @Column(columnDefinition = "TEXT")
    String description;

    public Recipe(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Recipe() {
        this.recipeIngredients = new ArrayList<>();
    }

    public void setRecipeIngredients(List<RecipeIngredients> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
        for (RecipeIngredients ingredient : recipeIngredients) {
            ingredient.setRecipe(this);
        }
    }
    public void addRecipeIngredient(RecipeIngredients recipeIngredients) {
        recipeIngredients.setRecipe(this);
        this.recipeIngredients.add(recipeIngredients);
    }
}
