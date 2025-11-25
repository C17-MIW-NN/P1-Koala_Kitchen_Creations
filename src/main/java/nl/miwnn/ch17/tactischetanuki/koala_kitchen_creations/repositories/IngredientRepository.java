package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Josse Muller
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    boolean existsByName(String ingredientName);
}
