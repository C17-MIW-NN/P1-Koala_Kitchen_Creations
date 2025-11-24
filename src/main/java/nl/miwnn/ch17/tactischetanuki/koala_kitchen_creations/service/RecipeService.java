package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service;

import lombok.RequiredArgsConstructor;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Josse Muller
 * Make child entities consistent when saving or retrieving Recipe entity
 */

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    public Optional<Recipe> findById(Long id) {
        return recipeRepository.findById(id);
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
}
