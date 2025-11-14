package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Category;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeIngredients;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeStep;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.CategoryRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeIngredientsRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeStepRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
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
    private final RecipeStepRepository recipeStepRepository;

    public InitializeController(RecipeRepository recipeRepository, CategoryRepository categoryRepository, RecipeStepRepository recipeStepRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.recipeStepRepository = recipeStepRepository;
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
        List<String> wrapSteps = List.of(
                "Verwarm de oven voor op 200°C en bekleed een bakplaat met bakpapier.",
                "Snijd de pompoen in blokjes en leg ze op de bakplaat.",
                "Besprenkel de pompoen met olijfolie, peper en zout, en rooster 20 minuten tot ze zacht zijn.",
                "Snijd ondertussen de avocado in plakjes en verkruimel de feta.",
                "Verwarm de wraps kort in een droge koekenpan of magnetron.",
                "Beleg de wraps met pompoen, avocado en feta, en strooi er wat gehakte amandelen over.",
                "Rol de wraps op en serveer direct, eventueel met een frisse yoghurtdressing."
        );

        for (int i = 0; i < wrapSteps.size(); i++) {
            RecipeStep step = new RecipeStep(lunch, wrapSteps.get(i), i + 1);
            recipeStepRepository.save(step);
        }

        Recipe voorgerecht = makeRecipe("Tomaat paprika soep", "Met geroosterde groenten " +
                "en een vleugje tijm zit hij boordevol smaak, zonder dat je er uren voor in de keuken hoeft te staan."
                , List.of(new RecipeIngredients("tomaatblokjes", "500 gram"),
                new RecipeIngredients("paprika", "3 stuks"),
                new RecipeIngredients("tijm", "paar takjes")),
                Set.of(dinnerCategory));
        List<String> stepsDescriptions = List.of(
                "Snijd de paprika’s in grove stukken.",
                "Leg de paprika en tomaatblokjes op een bakplaat, besprenkel met olijfolie en rooster 20 minuten op 200°C.",
                "Doe de geroosterde groenten in een pan, voeg een beetje bouillon toe en breng zachtjes aan de kook.",
                "Gebruik een staafmixer om de soep glad te pureren.",
                "Voeg de takjes tijm toe, breng op smaak met zout en peper.",
                "Verwijder de takjes tijm en serveer warm."
        );
        for (int i = 0; i < stepsDescriptions.size(); i++) {
            RecipeStep step = new RecipeStep(voorgerecht, stepsDescriptions.get(i), i + 1);
            recipeStepRepository.save(step);
        }

        Recipe stamppot = makeRecipe("Boerenkool stamppot","Deze klassieke Hollandse stamppot met " +
                "rookworst mag niet ontbreken tijdens de winter. Lekker met appelmoes of gebakken spekjes.",
                List.of(new RecipeIngredients("boerenkool", "400 gram"),
                new RecipeIngredients("aardappelen", "500 gram"),
                new RecipeIngredients("speklap", "4 stuks")),
                Set.of(dinnerCategory));
        List<String> stamppotSteps = List.of(
                "Schil de aardappelen en snijd ze in gelijke stukken.",
                "Breng een grote pan met water aan de kook en voeg de aardappelen toe.",
                "Leg de boerenkool bovenop de aardappelen en kook alles samen in ongeveer 20 minuten gaar.",
                "Bak ondertussen de speklappen goudbruin en krokant in een koekenpan.",
                "Giet de aardappelen en boerenkool af, maar bewaar een beetje kookvocht.",
                "Stamp de aardappelen en boerenkool fijn en voeg eventueel wat kookvocht of melk toe voor een smeuïge stamppot.",
                "Breng op smaak met zout, peper en een klontje boter.",
                "Serveer de stamppot met de gebakken speklappen en eventueel wat appelmoes."
        );

        for (int i = 0; i < stamppotSteps.size(); i++) {
            RecipeStep step = new RecipeStep(stamppot, stamppotSteps.get(i), i + 1);
            recipeStepRepository.save(step);
        }
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
