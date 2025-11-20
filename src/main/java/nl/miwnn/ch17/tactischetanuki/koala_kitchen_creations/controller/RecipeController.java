package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.*;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.CategoryRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.CategoryService;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.ImageService;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.RecipeStepService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.io.IOException;
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
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;
    private final CategoryService categoryService;

    public RecipeController(RecipeRepository recipeRepository, RecipeStepService recipeStepService,
                            CategoryRepository categoryRepository, CategoryService categoryService,
                            ImageService imageService) {
        this.recipeRepository = recipeRepository;
        this.recipeStepService = recipeStepService;
        this.categoryRepository = categoryRepository;
        this.imageService = imageService;
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
        Recipe newRecipe = new Recipe();
        newRecipe.addRecipeIngredient(new RecipeIngredients());
        return returnRecipeForm(datamodel, newRecipe);
    }

    public void processSubmittedImage(Recipe recipe, MultipartFile recipeImage, BindingResult result) {
        try {
            if (recipeImage != null && !recipeImage.isEmpty()) {
                Image image = imageService.saveImage(recipeImage);
                recipe.setImageURL("/image/" + image.getFileName());
            }
        } catch (IOException e) {
            result.rejectValue("imageURL", "imageNotSaved", "Image could not be saved");
        }
    }
    @PostMapping("/recipe/save")
    public String saveOrUpdateRecipe(@ModelAttribute("formRecipe") Recipe recipe,
                                     @RequestParam(value = "recipeImage", required = false) MultipartFile recipeImage,
                                     @RequestParam("selectedCategories") List<String> formSelectedCategories,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes )  {
        if (!result.hasErrors()) {
            processSubmittedImage(recipe, recipeImage, result);
            Set<Category> selectedCategories = categoryService.findOrCreateByNames(formSelectedCategories);
            recipe.setCategories(selectedCategories);
            recipeRepository.save(recipe);
        } else {
            System.err.println("Error saving recipe: " + result.toString());

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
