package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeIngredients;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeIngredientsRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

/**
 * @author Jantine van der Schaaf
 * @author Josse Mulle
 * Handle requests regarding RecipeIngredients
 */

@Controller
@RequestMapping("/recipeIngredients")
public class RecipeIngredientsController {
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientsRepository recipeIngredientsRepository;


    public RecipeIngredientsController(RecipeRepository recipeRepository, RecipeIngredientsRepository
            recipeIngredientsRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeIngredientsRepository = recipeIngredientsRepository;
    }

    @GetMapping("/new/{recipeId}")
    private String createnewRecipeIngredients(@PathVariable("recipeId") Long recipeId) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if (optionalRecipe.isPresent()) {
            RecipeIngredients recipeIngredients = new RecipeIngredients(optionalRecipe.get());
            recipeIngredientsRepository.save(recipeIngredients);
        }
        return "redirect:/recipe/all";

    }
}
