package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import com.opencsv.CSVReader;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.*;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.CategoryService;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.ImageService;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.RecipeStepService;
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

@Controller
public class InitializeController {
    private final RecipeRepository recipeRepository;
    private final RecipeStepService recipeStepService;
    private final CategoryService categoryService;
    private final ImageService imageService;

    public InitializeController(RecipeRepository recipeRepository, RecipeStepService recipeStepService,
                                CategoryService categoryService, ImageService imageService) {
        this.recipeRepository = recipeRepository;
        this.recipeStepService = recipeStepService;
        this.categoryService = categoryService;
        this.imageService = imageService;
    }

    @EventListener
    private void seed(ContextRefreshedEvent ignoredEvent) {
        if (recipeRepository.count() == 0) {
            initializeDB();
        }
    }

    private void initializeDB() {
        List<Image> sampleImages = loadImages("/sampledata/images/");
        loadRecipes("sampledata/recipes.csv", sampleImages);
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
        return new RecipeIngredients(ingredientLine[0].trim(), ingredientLine[1].trim());
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

    private void loadRecipes(String filename, List<Image> sampleImages) {
        try (CSVReader reader = new CSVReader(new FileReader(new ClassPathResource(filename).getFile()))) {
            // skip header
            reader.skip(1);

            for (String[] recipeLine : reader) {
                Recipe recipe = parseRecipeLineAndMakeRecipe(recipeLine);
                Image randomImage = sampleImages.get((int) (Math.random() * sampleImages.size()));
                recipe.setImageURL("/image/" + randomImage.getFileName());
                recipeRepository.save(recipe);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
