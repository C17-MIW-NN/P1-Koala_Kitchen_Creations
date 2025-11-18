package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 * Test methods from the Recipe class
 */
class RecipeTest {

    @Test
    void testNumberOfRecipeStepWhenAddOneStep() {
        // Arrange
        Recipe zuurkool = new Recipe("Zuurkoolstamppot", "Een recept voor in de herfst");
        zuurkool.setRecipeSteps(new ArrayList<>());

        Recipe lasagne = new Recipe("Lasagne", "Heerlijk ovengerecht");
        lasagne.setRecipeSteps(new ArrayList<>());

        RecipeStep stap = new RecipeStep(zuurkool, "Voeg de rookworst toe");

        // Act
        zuurkool.getRecipeSteps().add(stap);

        // Assert
        assertEquals(1, zuurkool.getRecipeSteps().size());

        assertTrue(zuurkool.getRecipeSteps().contains(stap));

        assertEquals("Voeg de rookworst toe", zuurkool.getRecipeSteps().get(0).getStepDescription());

        assertEquals(0, lasagne.getRecipeSteps().size());
    }
}