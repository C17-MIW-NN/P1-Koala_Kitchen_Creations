package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Category;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeIngredients;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.CategoryRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeIngredientsRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 * Initialises the database with example data
 */

@Controller
public class InitializeController {
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;

    public InitializeController(RecipeRepository recipeRepository, CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
    }

    @EventListener
    private void seed(ContextRefreshedEvent ignoredEvent) {
        if (recipeRepository.count() == 0) {
            initializeDB();
        }
    }

    private void initializeDB() {
        Category lunchCategory = makeCategory("Lunch");
        Category dinnerCategory = makeCategory("Dinner");

        Recipe lunch = makeRecipe("Wraps met pompoen", "Laat je inspireren door de herfst " +
                "met deze wraps met pompoen. Met smaakvolle pompoenblokjes, romige avocado en afgemaakt met " +
                "feta en amandelen. Perfect voor een koude lunch.", List.of(
                        new RecipeIngredients("pompoen", "200 gram"),
                        new RecipeIngredients("advocado", "1 stuks"),
                        new RecipeIngredients("feta", "100 gram")),
                Set.of(lunchCategory));

        Recipe voorgerecht = makeRecipe("Tomaat paprika soep", "Met geroosterde groenten " +
                "en een vleugje tijm zit hij boordevol smaak, zonder dat je er uren voor in de keuken hoeft te staan."
                , List.of(new RecipeIngredients("tomaatblokjes", "500 gram"),
                new RecipeIngredients("paprika", "3 stuks"),
                new RecipeIngredients("tijm", "paar takjes")),
                Set.of(dinnerCategory));

        Recipe stamppot = makeRecipe("Boerenkool stamppot","Deze klassieke Hollandse stamppot met " +
                "rookworst mag niet ontbreken tijdens de winter. Lekker met appelmoes of gebakken spekjes.",
                List.of(new RecipeIngredients("boerenkool", "400 gram"),
                new RecipeIngredients("aardappelen", "500 gram"),
                new RecipeIngredients("speklap", "4 stuks")),
                Set.of(dinnerCategory));
    }

    private Recipe makeRecipe(String name, String description, List<RecipeIngredients> ingredients,
                              Set<Category> categories) {
        Recipe recipe = new Recipe(name, description);
        recipe.setRecipeIngredients(ingredients);
        recipe.setCategories(categories);
        recipeRepository.save(recipe);

        System.out.printf("Recipe: recipeId:%s, name: %s, description: %s",
                recipe.getRecipeId(), recipe.getName(), recipe.getDescription());
        return recipe;
    }
    private Category makeCategory(String name) {
        return categoryRepository.save(new Category(name));
    }
}
