package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Category;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeUser;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.CategoryRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 * View category overview or all recipes belonging to a category
 */
@Controller
@RequestMapping("/category")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/all")
    public String showCategoryOverview(Model dataModel) {
        List<Category> categories = categoryRepository.findAll();
        dataModel.addAttribute("categories", categories);
        dataModel.addAttribute("formCategory", new Category());
        return "categoryOverview";
    }

    @GetMapping("/{categoryName}/recipes")
    public String showRecipesInCategory(@PathVariable String categoryName, Model dataModel) {
        Optional<Category> maybeCategory = categoryRepository.findByName(categoryName);
        if (maybeCategory.isEmpty()) {
            return "redirect:/category/all";
        }
        dataModel.addAttribute("category", maybeCategory.get());
        dataModel.addAttribute("recipes", maybeCategory.get().getRecipes());
        return "categoryRecipes";
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
