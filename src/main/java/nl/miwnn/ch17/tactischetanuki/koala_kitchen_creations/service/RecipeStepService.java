package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service;


import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeStep;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeStepRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeStepService {
    private final RecipeStepRepository recipeStepRepository;

    public RecipeStepService(RecipeStepRepository recipeStepRepository) {
        this.recipeStepRepository = recipeStepRepository;
    }

    public List<RecipeStep> getStepsByRecipe(Long recipeId) {
        return recipeStepRepository.findByRecipeRecipeId(recipeId);
    }

    public RecipeStep addStep(RecipeStep recipeStep) {
        return recipeStepRepository.save(recipeStep);
    }

    public void deleteStep(Long recipeStepId) {
        recipeStepRepository.deleteById(recipeStepId);
    }
}
