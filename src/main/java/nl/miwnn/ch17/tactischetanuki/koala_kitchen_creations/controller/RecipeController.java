package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Category;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.CategoryRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeStep;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.CategoryService;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.RecipeStepService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 * Handle requests regarding recipes
 */

@Controller
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final RecipeStepService recipeStepService;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    public RecipeController(RecipeRepository recipeRepository, RecipeStepService recipeStepService, CategoryRepository categoryRepository, CategoryService categoryService) {
        this.recipeRepository = recipeRepository;
        this.recipeStepService = recipeStepService;
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
    }

    @GetMapping({"/recipe/all", "/"})
    private String showRecipeOverview(Model datamodel) {
        ArrayList<Recipe> recipes = new ArrayList<>();

        datamodel.addAttribute("recipes", recipeRepository.findAll());
        return "recipeList";
    }

    @GetMapping("/recipe/add")
    public String showRecipeForm(Model datamodel) {
        return returnRecipeForm(datamodel, new Recipe());
    }

    @PostMapping("/recipe/save")
    public String saveOrUpdateRecipe(@ModelAttribute("formRecipe") Recipe recipe,
                                     @RequestParam("selectedCategories") List<String> formSelectedCategories,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes) {
        if (!result.hasErrors()) {
            Set<Category> selectedCategories = categoryService.findOrCreateByNames(formSelectedCategories);
            recipe.setCategories(selectedCategories);
            recipeRepository.save(recipe);
        } else {
            System.err.println("Error saving recipe: " + result.toString());;
        }
        redirectAttributes.addAttribute("recipeId", recipe.getRecipeId());
        return "redirect:/recipe/detail/{recipeId}";
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
            return returnRecipeForm(datamodel, recipe);
        }

        return "redirect:/recipe/all";
    }

    private String returnRecipeForm(Model datamodel, Recipe recipe) {
        datamodel.addAttribute("formRecipe", recipe);
        datamodel.addAttribute("availableCategories", categoryRepository.findAll());
        return "recipeForm";
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
}
