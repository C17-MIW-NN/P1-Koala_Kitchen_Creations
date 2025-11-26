package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.*;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.IngredientRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeUserRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
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

@RequiredArgsConstructor
@Controller
public class InitializeController {
    private final RecipeRepository recipeRepository;
    private final RecipeStepService recipeStepService;
    private final CategoryService categoryService;
    private final ImageService imageService;
    private final RecipeUserService recipeUserService;
    private final IngredientRepository ingredientRepository;
    private final IngredientService ingredientService;

    @EventListener
    private void seed(ContextRefreshedEvent ignoredEvent) {
        if (recipeRepository.count() == 0) {
            initializeDB();
        }
    }

    private void initializeDB() {
        List<RecipeUser> sampleUsers = new ArrayList<>();
        sampleUsers.add(makeUser("Kees", "KeesPL"));
        sampleUsers.add(makeUser("Barbara", "Barbara2000"));

        List<Image> sampleImages = loadImages("/sampledata/images/");
        loadRecipes("sampledata/recipes_50_detailed.csv", sampleImages, sampleUsers);
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

    private RecipeIngredients makeRecipeIngredient(String description) {
        String[] ingredientLine = description.split(":");
        String[] quantityAndUnit = ingredientLine[1].trim().split(" ");
        assert(quantityAndUnit.length == 2);
        String quantity = quantityAndUnit[0].trim();
        String unit = quantityAndUnit[1].trim();
        String ingredientName = ingredientLine[0].trim();
        Ingredient ingredient = ingredientService.findOrCreateByName(ingredientName, unit);
        return new RecipeIngredients(ingredient, quantity, unit);
    }

    private RecipeUser makeUser(String username, String password) {
        RecipeUser user = new RecipeUser();

        user.setUsername(username);
        user.setPassword(password);

        recipeUserService.saveUser(user);
        return user;
    }

    private List<Image> loadImages(String folderName) {

        List<Image> loadedImages = new ArrayList<>();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources("classpath:" + folderName + "*");
            for (Resource r : resources) {
                loadedImages.add(imageService.saveImage(r));
                System.out.println("Loaded sample image: " + r.getFilename());
            }
        } catch (IOException ioException) {
            throw new RuntimeException("Error loading sample files: " + ioException.getMessage());
        }
        return loadedImages;
    }

    private Recipe parseRecipeLineAndMakeRecipe(String[] recipeLine) {
        String name = recipeLine[0];
        String description = recipeLine[1];
        Set<Category> categories = categoryService.findOrCreateByNames(List.of(recipeLine[2].split(";")));
        List<RecipeIngredients> recipeIngredients = Stream.of(recipeLine[3].split(";"))
                .map(this::makeRecipeIngredient).toList();
        List<RecipeStep> steps = recipeStepService.createFromStrings(List.of(recipeLine[4].split(";")));
        return makeRecipe(name, description, categories, recipeIngredients, steps);
    }

    private void loadRecipes(String filename, List<Image> sampleImages, List<RecipeUser> sampleUsers) {
        try (CSVReader reader = new CSVReader(new FileReader(new ClassPathResource(filename).getFile()))) {
            // skip header
            reader.skip(1);

            for (String[] recipeLine : reader) {
                // TODO remove this
                if (Math.random() > 0.05) {
                    continue;
                }
                Recipe recipe = parseRecipeLineAndMakeRecipe(recipeLine);
                Image randomImage = sampleImages.get((int) (Math.random() * sampleImages.size()));
                recipe.setImageURL("/image/" + randomImage.getFileName());
                RecipeUser randomUser = sampleUsers.get((int) (Math.random() * sampleUsers.size()));
                recipe.setAuthor(randomUser);
                recipeRepository.save(recipe);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
