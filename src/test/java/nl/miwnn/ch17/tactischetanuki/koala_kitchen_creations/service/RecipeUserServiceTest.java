package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeUser;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author Jantine van der Schaaf
 * Doel methode
 */

@ExtendWith(MockitoExtension.class)
class RecipeUserServiceTest {

    @Mock
    private RecipeUserRepository recipeUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeUserService recipeUserService;

    private RecipeUser user;
    Recipe recipe1;
    Recipe recipe2;

    @BeforeEach
    void setUp() {
        user = new RecipeUser();
        user.setUsername("john");

        recipe1 = new Recipe();
        recipe1.setRecipeId(1L);

        recipe2 = new Recipe();
        recipe2.setRecipeId(2L);
    }

    @Test
    void testAddTwoFavorites() {
        // Arrange
        when(recipeUserRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe1));
        when(recipeRepository.findById(2L)).thenReturn(Optional.of(recipe2));

        // Act
        recipeUserService.addFavorite("john", 1L);
        recipeUserService.addFavorite("john", 2L);

        // Assert
        assertEquals(2, user.getFavorites().size());
        assertTrue(user.getFavorites().contains(recipe1));
        assertTrue(user.getFavorites().contains(recipe2));
    }
}
