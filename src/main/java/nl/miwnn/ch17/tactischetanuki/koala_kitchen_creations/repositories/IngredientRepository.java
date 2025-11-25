package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Josse Muller
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    boolean existsByName(String ingredientName);
    Optional<Ingredient> findByName(String name);
}
