package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeIngredients;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeIngredientsRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 * Initialises the database with example data
 */

@Controller
public class InitializeController {
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientsRepository recipeIngredientsRepository;

    public InitializeController(RecipeRepository recipeRepository, RecipeIngredientsRepository recipeIngredientsRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeIngredientsRepository = recipeIngredientsRepository;
    }

    @EventListener
    private void seed(ContextRefreshedEvent ignoredEvent) {
        if (recipeRepository.count() == 0) {
            initializeDB();
        }
    }

    private void initializeDB() {
        Recipe lunch = makeRecipe("Wraps met pompoen", "Laat je inspireren door de herfst " +
                "met deze wraps met pompoen. Met smaakvolle pompoenblokjes, romige avocado en afgemaakt met " +
                "feta en amandelen. Perfect voor een koude lunch.", List.of(
                        new RecipeIngredients("pompoen", "200 gram"),
                        new RecipeIngredients("advocado", "1 stuks"),
                        new RecipeIngredients("feta", "100 gram")));

        Recipe voorgerecht = makeRecipe("Tomaat paprika soep", "Met geroosterde groenten " +
                "en een vleugje tijm zit hij boordevol smaak, zonder dat je er uren voor in de keuken hoeft te staan."
                , List.of(new RecipeIngredients("tomaatblokjes", "500 gram"),
                new RecipeIngredients("paprika", "3 stuks"),
                new RecipeIngredients("tijm", "paar takjes")));

        Recipe stamppot = makeRecipe("Boerenkool stamppot","Deze klassieke Hollandse stamppot met " +
                "rookworst mag niet ontbreken tijdens de winter. Lekker met appelmoes of gebakken spekjes.",
                List.of(new RecipeIngredients("boerenkool", "400 gram"),
                new RecipeIngredients("aardappelen", "500 gram"),
                new RecipeIngredients("speklap", "4 stuks")));
    }

    private Recipe makeRecipe(String name, String description, List<RecipeIngredients> ingredients) {
        Recipe recipe = new Recipe(name, description);

        recipeRepository.save(recipe);
        for (RecipeIngredients recipeIngredients: ingredients) {
            recipeIngredients.setRecipe(recipe);
            recipeIngredientsRepository.save(recipeIngredients);
        }

        System.out.printf("Recipe: recipeId:%s, name: %s, description: %s",
                recipe.getRecipeId(), recipe.getName(), recipe.getDescription());

        return recipe;
    }

}
