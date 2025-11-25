package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.dto;

import jakarta.persistence.*;
import lombok.*;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Category;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeIngredients;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeStep;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Josse Muller
 * Contains info about recipe and its ingredients, categories and steps
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDetailDto {

    private Long recipeId;
    private String name;
    private String description;
    private String imageURL;
    private List<String> recipeSteps = new ArrayList<>();
    private List<RecipeIngredientDto> recipeIngredients = new ArrayList<>();
    private List<String> categories = new ArrayList<>();

}
