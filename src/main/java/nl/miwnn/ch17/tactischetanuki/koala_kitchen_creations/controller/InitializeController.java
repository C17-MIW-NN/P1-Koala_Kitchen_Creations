package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import com.opencsv.CSVReader;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Category;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeIngredients;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeStep;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.CategoryRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeStepRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.CategoryService;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.RecipeStepService;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 * Initialises the database with example data
 */

@Controller
public class InitializeController {
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final RecipeStepService recipeStepService;
    private final CategoryService categoryService;

    public InitializeController(RecipeRepository recipeRepository, CategoryRepository categoryRepository, RecipeStepRepository recipeStepRepository, RecipeStepService recipeStepService, CategoryService categoryService) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.recipeStepRepository = recipeStepRepository;
        this.recipeStepService = recipeStepService;
        this.categoryService = categoryService;
    }

    @EventListener
    private void seed(ContextRefreshedEvent ignoredEvent) {
        if (recipeRepository.count() == 0) {
            initializeDB();
        }
    }

    private void initializeDB() {
        loadRecipes("sampledata/recipes.csv");
    }

    private Recipe makeRecipe(String name, String description, Set<Category> categories,
                              List<RecipeIngredients> ingredients, List<RecipeStep> steps) {
        Recipe recipe = new Recipe(name, description);
        recipe.setCategories(categories);
        recipe.setRecipeIngredients(ingredients);
        recipe.setRecipeSteps(steps);
        recipeRepository.save(recipe);

         return recipe;
    }

    private Category makeCategory(String name) {
        return categoryRepository.save(new Category(name));
    }

    private RecipeIngredients makeRecipeIngredient(String description) {
        String[] ingredientLine = description.split(":");
        return new RecipeIngredients(ingredientLine[0].trim(), ingredientLine[1].trim());
    }

    private void loadRecipes(String filename) {
        List<Recipe> recipes = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(new ClassPathResource(filename).getFile()))) {
            // skip header
            reader.skip(1);

            for (String[] recipeLine : reader) {
                String name = recipeLine[0];
                String description = recipeLine[1];
                Set<Category> categories = categoryService.findOrCreateByNames(List.of(recipeLine[2].split(";")));
                List<RecipeIngredients> recipeIngredients = Stream.of(recipeLine[3].split(";"))
                        .map(this::makeRecipeIngredient).toList();
                List<RecipeStep> steps = recipeStepService.createFromStrings(List.of(recipeLine[4].split(";")));
                makeRecipe(name, description, categories, recipeIngredients, steps);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
