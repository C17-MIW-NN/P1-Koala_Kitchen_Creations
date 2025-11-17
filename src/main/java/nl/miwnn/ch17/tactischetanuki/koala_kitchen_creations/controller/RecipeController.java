package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeIngredients;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeStep;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.RecipeStepService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 * Handle requests regarding recipes
 */

@Controller
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final RecipeStepService recipeStepService;

    public RecipeController(RecipeRepository recipeRepository, RecipeStepService recipeStepService) {
        this.recipeRepository = recipeRepository;
        this.recipeStepService = recipeStepService;
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
            Recipe recipe = optionalRecipe.get();
            recipe.addRecipeIngredient(new RecipeIngredients());
            datamodel.addAttribute("formRecipe", recipe);
            return "recipeForm";
        }

        return "redirect:/recipe/all";
    }

    @GetMapping("/recipe/detail/{recipeId}")
    public String showRecipeDetail(@PathVariable Long recipeId, Model model) {
        Optional<Recipe> recipeOpt = recipeRepository.findById(recipeId);
        if (recipeOpt.isEmpty()) {
            return "redirect:/recipe/all";
        }
        Recipe recipe = recipeOpt.get();

        List<RecipeStep> steps = recipeStepService.getStepsByRecipe(recipeId);

        model.addAttribute("recipe", recipe);
        model.addAttribute("steps", steps);

        return "recipeDetail";
    }

//    @GetMapping("/recipe/detail/{recipeId}")
//    public String showRecipeDetailPage(@PathVariable("recipeId") Long recipeId, Model datamodel) {
//        Optional<Recipe> recipeToShow = recipeRepository.findById(recipeId);
//
//        if (recipeToShow.isEmpty()) {
//            return "redirect:/recipe/all";
//        }
//
//        datamodel.addAttribute("recipe", recipeToShow.get());
//
//        return "recipeDetail";
//    }

//    @GetMapping("/recipe/detail/{recipeId}/steps")
//    public String showRecipeSteps(@PathVariable("recipeId") Long recipeId, Model datamodel) {
//        List<RecipeStep> steps = recipeStepService.getStepsByRecipe(recipeId);
//
//        Recipe recipe = null;
//        if (!steps.isEmpty()) {
//            recipe = steps.get(0).getRecipe();
//        }
//        datamodel.addAttribute("recipe", recipe);
//        datamodel.addAttribute("steps", steps);
//    return "recipe/detail";
//    }
}
