package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import lombok.RequiredArgsConstructor;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.dto.RecipeDetailDto;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.*;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.CategoryRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.IngredientRepository;
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
import java.util.stream.Collectors;

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
    private final IngredientRepository ingredientRepository;
    private final RecipeUserService recipeUserService;


    @GetMapping({"/recipe/all", "/"})
    private String showRecipeOverview(Model datamodel, @AuthenticationPrincipal RecipeUser principal) {
        List<Recipe> allRecipes = recipeService.findAll();

        Set<Long> favoriteIds = new HashSet<>();
        if (principal != null) {
            RecipeUser userWithFavorites = recipeUserService.getUserWithFavorites(principal.getUsername());
            favoriteIds = userWithFavorites.getFavorites()
                            .stream()
                            .map(Recipe::getRecipeId)
                            .collect(Collectors.toSet());
        }

        List<Recipe> sortedRecipes = sortRecipesWithPriority(allRecipes, favoriteIds);

        datamodel.addAttribute("recipes", sortedRecipes);
        datamodel.addAttribute("favoriteIds", favoriteIds);
        return "recipeList";
    }

    private List<Recipe> sortRecipesWithPriority(List<Recipe> recipes, Set<Long> favoriteIds) {
        Map<Long, Integer> recipePriority = new HashMap<>();
        for (Recipe r : recipes) {
            recipePriority.put(r.getRecipeId(), favoriteIds.contains(r.getRecipeId()) ? 2 : 1);
        }

        return recipes.stream()
                .sorted(Comparator.comparingInt((Recipe r) -> recipePriority.get(r.getRecipeId()))
                        .reversed()
                        .thenComparing(Recipe::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }



    @GetMapping("/recipe/add")
    public String showRecipeForm(Model datamodel) {
        return returnRecipeForm(datamodel, recipeService.newRecipe());
    }

    private boolean canEdit(Long recipeId, RecipeUser user) {
        if (user == null) {
            return false;
        } else if (recipeId == null) {
            return true;
        } else {
            return recipeService.findById(recipeId).map((recipeDetailDto ->
                    recipeDetailDto.getAuthor().getId().equals(user.getUserId()))).orElse(false);
        }
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

    @GetMapping("/recipe/copy/{recipeId}")
    public String copyRecipe (@PathVariable("recipeId") Long recipeId,
                              @AuthenticationPrincipal RecipeUser principal) {
        if (principal == null) {
            return "redirect:/recipe/detail/" + recipeId;
        }
        Long copyId = recipeService.copyRecipe(recipeId, principal).orElseThrow().getRecipeId();
        return "redirect:/recipe/edit/" + copyId;
    }
    @PostMapping("/recipe/save")
    public String saveOrUpdateRecipe(@ModelAttribute("formRecipe") RecipeDetailDto recipe,
                                     @RequestParam(value = "recipeImage", required = false) MultipartFile recipeImage,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes,
                                     @AuthenticationPrincipal RecipeUser principal
                                     )  {
        if (!result.hasErrors() && canEdit(recipe.getRecipeId(), principal)) {
            Optional<String> newImageURL = processSubmittedImage(recipeImage, result);
            newImageURL.ifPresent(recipe::setImageURL);
            Recipe savedRecipe = recipeService.save(recipe, Optional.of(principal));
            savedRecipe.setAuthor(principal);
            redirectAttributes.addAttribute("recipeId", savedRecipe.getRecipeId());
            return "redirect:/recipe/detail/{recipeId}";
        } else {
            System.err.println("Error saving recipe: " + result.toString());
            return "redirect:/";
        }

    }

    @GetMapping("/recipe/delete/{recipeId}")
    public String deleteRecipe(@PathVariable("recipeId") Long recipeId,
                               @AuthenticationPrincipal RecipeUser principal) {
        if (canEdit(recipeId, principal)) {
            recipeService.deleteById(recipeId);
        } else {
            System.out.println("Not allowed to delete recipe.");
            return "redirect:/recipe/detail/" + recipeId;
        }
        return "redirect:/recipe/all";
    }

    @GetMapping("/recipe/edit/{recipeId}")
    public String showEditRecipeform(@PathVariable("recipeId") Long recipeId,
                                     @AuthenticationPrincipal RecipeUser principal,
                                     Model datamodel) {
        if (recipeId != null && canEdit(recipeId, principal)) {
            // Cannot throw if canEdit returns true
            RecipeDetailDto recipe = recipeService.findById(recipeId).orElseThrow();
            return returnRecipeForm(datamodel, recipe);
        }
        return "redirect:/recipe/all";
    }

    private String returnRecipeForm(Model datamodel, RecipeDetailDto recipe) {
        datamodel.addAttribute("formRecipe", recipe);
        datamodel.addAttribute("availableCategories", categoryRepository.findAll());
        datamodel.addAttribute("knownIngredients", ingredientRepository.findAll());
        return "recipeForm";
    }

    @GetMapping("/recipe/favorites")
    public String showFavorites(@AuthenticationPrincipal RecipeUser principal, Model model) {
        RecipeUser userWithFavorites = recipeUserService.getUserWithFavorites(principal.getUsername());
        model.addAttribute("favorites", userWithFavorites.getFavorites());
        return "userFavorites";
    }

    @GetMapping("/recipe/detail/{recipeId}/portionCount/{portionCount}")
    public String showRecipeDetailWithPortionCount(@PathVariable Long recipeId, @PathVariable Integer portionCount,
                                                   @AuthenticationPrincipal RecipeUser principal,
                                                   Model model) {
        Optional<RecipeDetailDto> recipeOpt = recipeService.findById(recipeId);
        if (recipeOpt.isEmpty()) {
            return "redirect:/recipe/all";
        }
        RecipeDetailDto originalRecipe = recipeOpt.get();
        RecipeDetailDto convertedRecipe = recipeService.convertPortionCount(originalRecipe, portionCount);
        return showRecipeDetails(recipeId, principal, model, convertedRecipe);
    }

    @GetMapping("/recipe/detail/{recipeId}")
    public String showRecipeDetail(@PathVariable Long recipeId, @AuthenticationPrincipal RecipeUser principal,
                                   Model model) {
        Optional<RecipeDetailDto> recipeOpt = recipeService.findById(recipeId);
        if (recipeOpt.isEmpty()) {
            return "redirect:/recipe/all";
        }
        RecipeDetailDto recipe = recipeOpt.get();

        return showRecipeDetails(recipeId, principal, model, recipe);
    }

    private String showRecipeDetails(Long recipeId, RecipeUser principal, Model model, RecipeDetailDto recipe) {
        model.addAttribute("recipe", recipe);
        model.addAttribute("userCanEditRecipe", canEdit(recipeId, principal));

        if (principal != null) {
            RecipeUser userWithFavorites = recipeUserService.getUserWithFavorites(principal.getUsername());
            boolean isFavorite = userWithFavorites.getFavorites().stream().anyMatch(
                    (favoriteRecipe) -> favoriteRecipe.getRecipeId().equals(recipeId));
            model.addAttribute("isFavorite", isFavorite);
        }
        return "recipeDetail";
    }


    @GetMapping("/recipe/{recipeId}/favorite/add")
    public String addFavorite(@PathVariable Long recipeId,
                              @AuthenticationPrincipal RecipeUser principal) {
        if (principal != null) {
            recipeUserService.addFavorite(principal.getUsername(), recipeId);
        }
        return "redirect:/recipe/detail/" + recipeId;
    }

    @GetMapping("/recipe/{recipeId}/favorite/remove")
    public String removeFavorite(@PathVariable Long recipeId,
                                 @AuthenticationPrincipal RecipeUser principal) {
        if (principal != null) {
            recipeUserService.removeFavorite(principal.getUsername(), recipeId);
        }
        return "redirect:/recipe/detail/" + recipeId;
    }

    @GetMapping("/recipe/favorites/remove/{recipeId}")
    public String removeFromFavorite(@PathVariable Long recipeId,
                                 @AuthenticationPrincipal RecipeUser principal) {
        if (principal != null) {
            recipeUserService.removeFavorite(principal.getUsername(), recipeId);
        }
        return "redirect:/recipe/favorites";
    }
}
