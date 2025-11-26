package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service;

import lombok.RequiredArgsConstructor;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Ingredient;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeIngredients;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.IngredientRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Josse Muller
 */

@Component
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    public Ingredient findOrCreateByName(String name, String suggestedUnit) {
        return ingredientRepository.findByName(name).orElseGet(() ->
                ingredientRepository.save(new Ingredient(name, suggestedUnit)));
    }
}
