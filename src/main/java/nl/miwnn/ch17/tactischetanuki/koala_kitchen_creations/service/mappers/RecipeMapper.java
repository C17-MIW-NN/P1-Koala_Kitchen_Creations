package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.mappers;

import lombok.RequiredArgsConstructor;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.dto.RecipeDetailDto;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Category;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeIngredients;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeStep;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeIngredientsRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.CategoryService;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.RecipeStepService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author Josse Muller
 * Map between dto and entity. Categories and steps are updated by service
 */

@RequiredArgsConstructor
@Component
public class RecipeMapper {
    private final RecipeIngredientMapper recipeIngredientMapper;
    private final CategoryService categoryService;
    private final RecipeStepService recipeStepService;
    private final RecipeIngredientsRepository recipeIngredientsRepository;

    public RecipeDetailDto toDto(Recipe entity) {
        return new RecipeDetailDto(
                entity.getRecipeId(),
                entity.getName(),
                entity.getDescription(),
                entity.getImageURL(),
                entity.getNumberOfPortions(),
                entity.getRecipeSteps().stream().map(RecipeStep::getStepDescription).toList(),
                entity.getRecipeIngredients().stream().map(recipeIngredientMapper::toDto).toList(),
                entity.getCategories().stream().map(Category::getName).toList(),
                RecipeUserMapper.toAuthorDTO(entity.getAuthor())
        );
    }
    public Recipe updateRecipe(RecipeDetailDto dto, Recipe existingRecipe) {
        existingRecipe.setName(dto.getName());
        existingRecipe.setDescription(dto.getDescription());
        existingRecipe.setImageURL(dto.getImageURL());
        existingRecipe.setNumberOfPortions(dto.getNumberOfPortions());

        Set<Category> categories = categoryService.findOrCreateByNames(dto.getCategories());
        existingRecipe.getCategories().clear();
        categories.forEach(existingRecipe::addCategory);

        List<RecipeStep> recipeSteps = recipeStepService.createFromStrings(dto.getRecipeSteps());
        existingRecipe.getRecipeSteps().clear();
        recipeSteps.forEach(existingRecipe::addRecipeStep);

        // TODO: If user submits a RI linked to another recipe, reject it instead of stealing it
        List<RecipeIngredients> recipeIngredients = dto.getRecipeIngredients().stream()
                .map(recipeIngredientMapper::toEntity).toList();
        existingRecipe.getRecipeIngredients().clear();
        recipeIngredients.forEach((recipeIngredient) -> {
            recipeIngredient = recipeIngredientsRepository.save(recipeIngredient);
            existingRecipe.addRecipeIngredient(recipeIngredient);
        });

        return existingRecipe;
    }
}
