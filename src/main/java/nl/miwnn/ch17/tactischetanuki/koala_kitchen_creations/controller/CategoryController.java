package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Category;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

/**
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
        return "categoryOverview";
    }

    @GetMapping("/{categoryId}/recipes")
    public String showRecipesInCategory(@PathVariable Long categoryId, Model dataModel) {
        Optional<Category> maybeCategory = categoryRepository.findById(categoryId);
        if (maybeCategory.isEmpty()) {
            return "redirect:/category/all";
        }
        dataModel.addAttribute("category", maybeCategory.get());
        dataModel.addAttribute("recipes", maybeCategory.get().getRecipes());
        return "categoryRecipes";
    }
}
