package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import lombok.RequiredArgsConstructor;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Category;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeUser;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.CategoryRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.RecipeUserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 * View category overview or all recipes belonging to a category
 */
@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final RecipeUserService recipeUserService;

    @GetMapping("/all")
    public String showCategoryOverview(Model dataModel, @AuthenticationPrincipal RecipeUser principal) {
        List<Category> categories = categoryRepository.findAll();

        Map<Long, String> categoryImages = new HashMap<>();
        String image = "/images/default-category.png";
        for (Category category : categories) {
            List<String> imageList = category.getRecipes().stream()
                    .map(Recipe::getImageURL).filter(Objects::nonNull).toList();
            if (!imageList.isEmpty()) {
                image = imageList.get((int) (Math.random() * imageList.size()));
            }
            categoryImages.put(category.getCategoryId(), image);
        }
        Set<Recipe> favoriteRecipes = recipeUserService.getUserWithFavorites(principal.getUsername()).getFavorites();
        Map<Long, Integer> favoriteCounts = getFavoriteCounts(favoriteRecipes);
        dataModel.addAttribute("categories", categories);
        dataModel.addAttribute("formCategory", new Category());
        dataModel.addAttribute("categoryImages", categoryImages);
        dataModel.addAttribute("favoriteCounts", favoriteCounts);
        return "categoryOverview";
    }

    @GetMapping("/{categoryName}/recipes")
    public String showRecipesInCategory(@PathVariable String categoryName, @AuthenticationPrincipal RecipeUser principal,
                                       Model dataModel) {
        Optional<Category> maybeCategory = categoryRepository.findByName(categoryName);
        if (maybeCategory.isEmpty()) {
            return "redirect:/category/all";
        }

        Category category = maybeCategory.get();
        Set<Recipe> recipesSet = category.getRecipes();
        List<Recipe> recipes = new ArrayList<>(recipesSet);

        Set<Long> favoriteIds = getFavoriteIds(principal);


        dataModel.addAttribute("category", category);
        dataModel.addAttribute("recipes", recipes);
        dataModel.addAttribute("favoriteIds", favoriteIds);

        return "categoryRecipes";
    }

    private Set<Long> getFavoriteIds(RecipeUser principal) {
        if (principal != null) {
            RecipeUser userWithFavorites = recipeUserService.getUserWithFavorites(principal.getUsername());
            return userWithFavorites.getFavorites()
                    .stream()
                    .map(Recipe::getRecipeId)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    private Map<Long, Integer> getFavoriteCounts(Set<Recipe> favoriteRecipes) {
        return favoriteRecipes.stream()
                .map(Recipe::getCategories).flatMap(Collection::stream)
                .collect(Collectors.toMap(
                        Category::getCategoryId,
                        r -> 1,
                        Integer::sum
                ));
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category formCategory, BindingResult result,
                               @AuthenticationPrincipal RecipeUser principal) {
        if (!result.hasErrors() && principal != null) {
            categoryRepository.save(formCategory);
        }
        return "redirect:/category/all";
    }
}
