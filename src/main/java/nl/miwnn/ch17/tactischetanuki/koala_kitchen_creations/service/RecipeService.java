package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.dto.RecipeDetailDto;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.mapper.RecipeMapper;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Category;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeIngredients;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeStep;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeIngredientsRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Josse Muller
 * Make child entities consistent when saving or retrieving Recipe entity
 */

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeMapper recipeMapper;
    private final RecipeRepository recipeRepository;
    private final CategoryService categoryService;
    private final RecipeStepService recipeStepService;
    private final RecipeIngredientsRepository recipeIngredientsRepository;

    public Optional<RecipeDetailDto> findById(Long id) {
        return recipeRepository.findById(id).map(recipeMapper::toDto);
    }
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }
    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Transactional
    public Recipe save(RecipeDetailDto dto) {
        Recipe existingRecipe;
        if (dto.getRecipeId() == null) {
            existingRecipe = new Recipe();
        } else {
            existingRecipe = recipeRepository.findById(dto.getRecipeId()).orElseGet(Recipe::new);
        }
        Recipe updatedRecipe = recipeMapper.updateRecipe(dto, existingRecipe);

        Set<Category> categories = categoryService.findOrCreateByNames(dto.getCategories());
        updatedRecipe.getCategories().clear();
        categories.forEach(updatedRecipe::addCategory);

        List<RecipeStep> recipeSteps = recipeStepService.createFromStrings(dto.getRecipeSteps());
        updatedRecipe.getRecipeSteps().clear();
        recipeSteps.forEach(updatedRecipe::addRecipeStep);

        existingRecipe.getRecipeIngredients().clear();
        dto.getRecipeIngredients().forEach((recipeIngredients) -> {
            if (recipeIngredients.getRecipeIngredientsId() != null) {
                Optional<RecipeIngredients> savedRecipeIngredients = recipeIngredientsRepository.findById(recipeIngredients.getRecipeIngredientsId());
            }
            // TODO check that if child exists, it is linked to this recipe
            recipeIngredients = recipeIngredientsRepository.save(recipeIngredients);
            existingRecipe.addRecipeIngredient(recipeIngredients);
        });
        return updatedRecipe;
    }
    public RecipeDetailDto newRecipe() {
        RecipeDetailDto newRecipe = new RecipeDetailDto();
        newRecipe.setRecipeIngredients(List.of(new RecipeIngredients()));
        return newRecipe;
    }
}
