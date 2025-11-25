package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.dto.RecipeDetailDto;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.dto.RecipeIngredientDto;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.mapper.RecipeMapper;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
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

        return updatedRecipe;
    }
    public RecipeDetailDto newRecipe() {
        RecipeDetailDto newRecipe = new RecipeDetailDto();
        newRecipe.setRecipeIngredients(List.of(new RecipeIngredientDto()));
        return newRecipe;
    }
}
