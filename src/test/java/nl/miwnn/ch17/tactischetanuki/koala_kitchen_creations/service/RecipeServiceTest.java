package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.*;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.mappers.RecipeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Josse Muller
 */

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock private RecipeRepository recipeRepository;
    @Mock RecipeMapper recipeMapper;

    @InjectMocks
    private RecipeService recipeService;


    @BeforeEach
    void setup() {
    }


    @Test()
    void copyRecipeCreatesNewRecipeWithNullIdAndUpdatedAuthorAndOtherFieldsCopiedAndNullChildIds() {
        // Arrange
        RecipeUser originalAuthor = new RecipeUser();
        originalAuthor.setUserId(1L);
        originalAuthor.setUsername("Original author");

        RecipeUser copyAuthor = new RecipeUser();
        copyAuthor.setUserId(2L);
        copyAuthor.setUsername("Original author");

        Recipe originalRecipe = getOriginalRecipe(originalAuthor);

        when(recipeRepository.findById(originalRecipe.getRecipeId())).thenReturn(Optional.of(originalRecipe));
        ArgumentCaptor<Recipe> copiedRecipeCaptor = ArgumentCaptor.forClass(Recipe.class);

        // Act
        recipeService.copyRecipe(originalRecipe.getRecipeId(), copyAuthor);

        // Assert
        verify(recipeRepository).save(copiedRecipeCaptor.capture());
        Recipe copiedRecipe = copiedRecipeCaptor.getValue();
        assertNull(copiedRecipe.getRecipeId());
        assertEquals(copiedRecipe.getDescription(), originalRecipe.getDescription());
        assertEquals(copiedRecipe.getAuthor(), copyAuthor);
        assertTrue(copiedRecipe.getName().startsWith(originalRecipe.getName()));
        assertTrue(copiedRecipe.getName().endsWith(copyAuthor.getUsername()));
        assertEquals(copiedRecipe.getRecipeIngredients().size(), originalRecipe.getRecipeIngredients().size());
        Stream<Long> copiedRecipeIngredientsIds = copiedRecipe.getRecipeIngredients().stream()
                .map(RecipeIngredients::getRecipeIngredientsId);
        assertTrue(copiedRecipeIngredientsIds.allMatch(Objects::isNull));

        Set<Ingredient> originalLinkedIngredients = originalRecipe.getRecipeIngredients().stream()
                .map(RecipeIngredients::getIngredient).collect(Collectors.toSet());
        Set<Ingredient> copyLinkedIngredients = copiedRecipe.getRecipeIngredients().stream()
                .map(RecipeIngredients::getIngredient).collect(Collectors.toSet());
        assertEquals(originalLinkedIngredients, copyLinkedIngredients);

        Set<String> originalSteps = originalRecipe.getRecipeSteps().stream().map(RecipeStep::getStepDescription).collect(Collectors.toSet());
        Set<String> copiedSteps =  copiedRecipe.getRecipeSteps().stream().map(RecipeStep::getStepDescription).collect(Collectors.toSet());
        assertEquals(originalSteps, copiedSteps);

        assertEquals(originalRecipe.getImageURL(), copiedRecipe.getImageURL());
    }

    private static Recipe getOriginalRecipe(RecipeUser originalAuthor) {
        Recipe originalRecipe = new Recipe("Original", "Original description");

        Ingredient ingredient1 = new Ingredient("Ingredient1", "unit1");
        Ingredient ingredient2 = new Ingredient("Ingredient2", "unit2");

        List<RecipeIngredients> originalRecipeIngredients = List.of(
                new RecipeIngredients(1L, ingredient1, "quantity1", ingredient1.getDefaultUnit()),
                new RecipeIngredients(2L, ingredient2, "quantity2", "unit2")
        );
        originalRecipe.setRecipeIngredients(originalRecipeIngredients);

        List<RecipeStep> originalSteps = List.of(new RecipeStep("step1"), new RecipeStep("step2"));
        originalRecipe.setRecipeSteps(originalSteps);

        originalRecipe.setAuthor(originalAuthor);

        originalRecipe.setImageURL("originalImageUrl");
        return originalRecipe;
    }
}