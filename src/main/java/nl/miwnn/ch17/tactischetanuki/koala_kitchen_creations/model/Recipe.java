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

    @OrderColumn
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeStep> recipeSteps;

    @Column(columnDefinition = "TEXT")
    String description;

    public Recipe(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public Recipe() {
        this.recipeIngredients = new ArrayList<>();
        this.recipeSteps = new ArrayList<>();
    }

    public List<Long> categoryIds() {
        return categories.stream().map(Category::getCategoryId).toList();
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

    public void setRecipeSteps(List<RecipeStep> recipeSteps) {
        this.recipeSteps = recipeSteps;
        for (RecipeStep step : recipeSteps) {
            step.setRecipe(this);
        }
    }

    public void addRecipeStep(RecipeStep step) {
        step.setRecipe(this);
        this.recipeSteps.add(step);
    }
}
