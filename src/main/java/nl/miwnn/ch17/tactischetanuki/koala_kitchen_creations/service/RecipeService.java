package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.dto.RecipeDetailDto;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.dto.RecipeIngredientDto;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.mappers.RecipeMapper;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeStep;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeUser;
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
    private final IngredientService ingredientService;

    public Optional<Recipe> copyRecipe(Long recipeId, RecipeUser newAuthor) {
        return recipeRepository.findById(recipeId).map((originalRecipe) -> {
            Recipe newRecipe = new Recipe(originalRecipe.getName() + " à la " + newAuthor.getUsername(),
                    originalRecipe.getDescription());
            newRecipe.setImageURL(originalRecipe.getImageURL());
            newRecipe.setAuthor(newAuthor);
            newRecipe.setCategories(new HashSet<>(originalRecipe.getCategories()));
            newRecipe.setRecipeSteps(originalRecipe.getRecipeSteps().stream()
                    .map(RecipeStep::getStepDescription).map(RecipeStep::new).toList());
            newRecipe.setRecipeIngredients(ingredientService.copyRecipeIngredients(originalRecipe.getRecipeIngredients()));
            return recipeRepository.save(newRecipe);
        });
    }

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
    public Recipe save(RecipeDetailDto dto, Optional<RecipeUser> author) {
        Recipe existingRecipe;
        if (dto.getRecipeId() == null) {
            existingRecipe = recipeRepository.save(new Recipe());
        } else {
            existingRecipe = recipeRepository.findById(dto.getRecipeId()).orElseGet(Recipe::new);
        }
        Recipe updatedRecipe =  recipeMapper.updateRecipe(dto, existingRecipe);
        author.ifPresent(updatedRecipe::setAuthor);
        return recipeRepository.save(updatedRecipe);
    }
    public RecipeDetailDto newRecipe() {
        RecipeDetailDto newRecipe = new RecipeDetailDto();
        newRecipe.setRecipeIngredients(List.of(new RecipeIngredientDto()));
        newRecipe.setRecipeSteps(List.of(""));
        return newRecipe;
    }
}
