package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 * Handle requests regarding recipes
 */

@Controller
public class RecipeController {

    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping({"/recipe/all", "/"})
    private String showRecipeOverview(Model datamodel) {
        ArrayList<Recipe> recipes = new ArrayList<>();

        datamodel.addAttribute("recipes", recipeRepository.findAll());
        return "recipeList";
    }

    @GetMapping("/recipe/add")
    public String showRecipeForm(Model datamodel) {
        datamodel.addAttribute("formRecipe", new Recipe());

        return "recipeForm";
    }

    @PostMapping("/recipe/save")
    public String saveOrUpdateRecipe(@ModelAttribute("formRecipe") Recipe recipe, BindingResult result) {
        if (!result.hasErrors()) {
            recipeRepository.save(recipe);
        }
        return "redirect:/recipe/all";
    }

    @GetMapping("/recipe/delete/{recipeId}")
    public String deleteRecipe(@PathVariable("recipeId") Long recipeId) {
        recipeRepository.deleteById(recipeId);
        return "redirect:/recipe/all";
    }

    @GetMapping("/recipe/edit/{recipeId}")
    public String showEditRecipeform(@PathVariable("recipeId") Long recipeId, Model datamodel) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if (optionalRecipe.isPresent()) {
            datamodel.addAttribute("formRecipe", optionalRecipe);
            return "recipeForm";
        }

        return "redirect:/recipe/all";
    }

    @GetMapping("/recipe/detail/{name}")
    public String showRecipeDetailPage(@PathVariable("name") String name, Model datamodel) {
        Optional<Recipe> recipeToShow = recipeRepository.findByName(name);

        if (recipeToShow.isEmpty()) {
            return "redirect:/recipe/all";
        }

        datamodel.addAttribute("recipe", recipeToShow.get());

        return "recipeDetails";
    }
}
