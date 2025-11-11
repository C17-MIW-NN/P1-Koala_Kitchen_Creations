package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 * Initialises the database with example data
 */

@Controller
public class InitializeController {
    private final RecipeRepository recipeRepository;

    public InitializeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
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
                "feta en amandelen. Perfect voor een koude lunch.");
        Recipe voorgerecht = makeRecipe("Tomaat paprika soep", "Met geroosterde groenten " +
                "en een vleugje tijm zit hij boordevol smaak, zonder dat je er uren voor in de keuken hoeft te staan.");
        Recipe stamppot = makeRecipe("Boerenkool stamppot","Deze klassieke Hollandse stamppot met " +
                "rookworst mag niet ontbreken tijdens de winter. Lekker met appelmoes of gebakken spekjes.");
    }

    private Recipe makeRecipe(String name, String description) {
        Recipe recipe = new Recipe(name, description);

        recipeRepository.save(recipe);

        System.out.printf("Recipe: recipeId:%s, name: %s, description: %s",
                recipe.getRecipeId(), recipe.getName(), recipe.getDescription());

        return recipe;
    }
}
