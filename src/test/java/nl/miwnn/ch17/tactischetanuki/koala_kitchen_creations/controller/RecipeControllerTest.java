package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.dto.RecipeDetailDto;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeUser;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.CategoryRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.IngredientRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.ImageService;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.RecipeService;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.RecipeStepService;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.RecipeUserService;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.mappers.RecipeUserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * @author Josse Muller
 */
@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {
    private static final long RECIPE_ID = 1L;
    @Mock RecipeService recipeService;
    @Mock RecipeStepService recipeStepService;
    @Mock CategoryRepository categoryRepository;
    @Mock ImageService imageService;
    @Mock IngredientRepository ingredientRepository;
    @Mock RecipeUserService recipeUserService;

    @InjectMocks
    private RecipeController recipeController;

    RecipeUser author;
    RecipeUser viewer;
    Recipe recipe;
    RecipeDetailDto recipeDto = new RecipeDetailDto();

    @BeforeEach
    void setup() {
        author = new RecipeUser();
        author.setUserId(1L);
        author.setUsername("Author");
        viewer = new RecipeUser();
        viewer.setUserId(2L);
        viewer.setUsername("Viewer");
        recipe = new Recipe("Name", "Decription");
        recipe.setRecipeId(RECIPE_ID);
        recipeDto.setRecipeId(RECIPE_ID);
        recipeDto.setAuthor(RecipeUserMapper.toAuthorDTO(author));
    }

    @Test
    void authorCanDeleteRecipe() {
        // Arrange
        when(recipeService.findById(RECIPE_ID)).thenReturn(Optional.of(recipeDto));
        // Act
        recipeController.deleteRecipe(RECIPE_ID, author);
        // Assert
        verify(recipeService).deleteById(RECIPE_ID);
    }

    @Test
    void nonAuthorCannotDeleteRecipe() {
        // Arrange
        when(recipeService.findById(RECIPE_ID)).thenReturn(Optional.of(recipeDto));
        // Act
        recipeController.deleteRecipe(RECIPE_ID, viewer);
        // Assert
        verify(recipeService, never()).deleteById(RECIPE_ID);
    }

    @Test
    void unauthenticatedUserCannotDeleteRecipe() {
        // Act
        recipeController.deleteRecipe(RECIPE_ID, null);
        // Assert
        verify(recipeService, never()).deleteById(RECIPE_ID);
    }
}