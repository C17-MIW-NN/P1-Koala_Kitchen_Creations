package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

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
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final RecipeUserService recipeUserService;
    private final RecipeRepository recipeRepository;

    public CategoryController(CategoryRepository categoryRepository, RecipeUserService recipeUserService, RecipeRepository recipeRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeUserService = recipeUserService;
        this.recipeRepository = recipeRepository;
    }

    @GetMapping("/all")
    public String showCategoryOverview(Model dataModel, @AuthenticationPrincipal RecipeUser principal) {
        List<Category> categories = categoryRepository.findAll();

        Map<Long, String> categoryImages = new HashMap<>();
        for (Category category : categories) {
            String image = category.getRecipes().stream()
                    .filter(r -> r.getImageURL() != null && !r.getImageURL().isEmpty())
                    .map(Recipe::getImageURL)
                    .findAny()
                    .orElse("/images/default-category.png");
            categoryImages.put(category.getCategoryId(), image);
        }
        Map<Long, Integer> favoriteCounts = getFavoriteCounts(recipeRepository.findAll(), principal);

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

    private Map<Long, Integer> getFavoriteCounts(List<Recipe> recipes, RecipeUser user) {
        return recipes.stream()
                .filter(r -> r.getFavoritedBy().contains(user))
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
