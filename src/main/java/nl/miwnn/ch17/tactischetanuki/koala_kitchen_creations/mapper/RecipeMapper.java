package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.mapper;

import lombok.RequiredArgsConstructor;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.dto.RecipeDetailDto;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Category;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeStep;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeIngredientsRepository;
import org.springframework.stereotype.Component;

/**
 * @author Josse Muller
 * Map between dto and entity. Categories and steps are updated by service
 */

@RequiredArgsConstructor
@Component
public class RecipeMapper {
    public RecipeDetailDto toDto(Recipe entity) {
        return new RecipeDetailDto(
                entity.getRecipeId(),
                entity.getName(),
                entity.getDescription(),
                entity.getImageURL(),
                entity.getRecipeSteps().stream().map(RecipeStep::getStepDescription).toList(),
                entity.getRecipeIngredients(),
                entity.getCategories().stream().map(Category::getName).toList()
        );
    }
    public Recipe updateRecipe(RecipeDetailDto dto, Recipe existingRecipe) {
        existingRecipe.setName(dto.getName());
        existingRecipe.setDescription(dto.getDescription());
        existingRecipe.setImageURL(dto.getImageURL());

        return existingRecipe;
    }
}
