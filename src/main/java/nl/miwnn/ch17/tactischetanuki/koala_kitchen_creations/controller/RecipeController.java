package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import lombok.RequiredArgsConstructor;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.dto.RecipeDetailDto;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.*;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.CategoryRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.CategoryService;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.ImageService;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.RecipeService;
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

@RequiredArgsConstructor
@Controller
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeStepService recipeStepService;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;
    private final CategoryService categoryService;


    @GetMapping({"/recipe/all", "/"})
    private String showRecipeOverview(Model datamodel) {
        ArrayList<Recipe> recipes = new ArrayList<>();

        datamodel.addAttribute("recipes", recipeService.findAll());
        return "recipeList";
    }

    @GetMapping("/recipe/add")
    public String showRecipeForm(Model datamodel) {
        return returnRecipeForm(datamodel, recipeService.newRecipe());
    }

    public Optional<String> processSubmittedImage(MultipartFile recipeImage, BindingResult result) {
        try {
            if (recipeImage != null && !recipeImage.isEmpty()) {
                Image image = imageService.saveImage(recipeImage);
                return Optional.of("/image/" + image.getFileName());
            }
        } catch (IOException e) {
            result.rejectValue("imageURL", "imageNotSaved", "Image could not be saved");
        }
        return Optional.empty();
    }
    @PostMapping("/recipe/save")
    public String saveOrUpdateRecipe(@ModelAttribute("formRecipe") RecipeDetailDto recipe,
                                     @RequestParam(value = "recipeImage", required = false) MultipartFile recipeImage,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes )  {
        if (!result.hasErrors()) {
            Optional<String> newImageURL = processSubmittedImage(recipeImage, result);
            newImageURL.ifPresent(recipe::setImageURL);
            recipeService.save(recipe);
        } else {
            System.err.println("Error saving recipe: " + result.toString());
        }
        redirectAttributes.addAttribute("recipeId", recipe.getRecipeId());
        return "redirect:/recipe/detail/{recipeId}";
    }

    @GetMapping("/recipe/delete/{recipeId}")
    public String deleteRecipe(@PathVariable("recipeId") Long recipeId) {
        recipeService.deleteById(recipeId);
        return "redirect:/recipe/all";
    }

    @GetMapping("/recipe/edit/{recipeId}")
    public String showEditRecipeform(@PathVariable("recipeId") Long recipeId, Model datamodel) {
        Optional<RecipeDetailDto> optionalRecipe = recipeService.findById(recipeId);

        if (optionalRecipe.isPresent()) {
            RecipeDetailDto recipe = optionalRecipe.get();
            return returnRecipeForm(datamodel, recipe);
        }

        return "redirect:/recipe/all";
    }

    private String returnRecipeForm(Model datamodel, RecipeDetailDto recipe) {
        datamodel.addAttribute("formRecipe", recipe);
        datamodel.addAttribute("availableCategories", categoryRepository.findAll());
        return "recipeForm";
    }

    @GetMapping("/recipe/detail/{recipeId}")
    public String showRecipeDetail(@PathVariable Long recipeId, Model model) {
        Optional<RecipeDetailDto> recipeOpt = recipeService.findById(recipeId);
        if (recipeOpt.isEmpty()) {
            return "redirect:/recipe/all";
        }
        RecipeDetailDto recipe = recipeOpt.get();

        List<RecipeStep> steps = recipeStepService.getStepsByRecipe(recipeId);

        model.addAttribute("recipe", recipe);
        model.addAttribute("steps", steps);

        return "recipeDetail";
    }
}
