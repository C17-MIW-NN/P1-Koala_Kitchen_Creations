package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 */
public interface RecipeUserRepository extends JpaRepository<RecipeUser, Long> {
    Optional<RecipeUser> findByUsername(String username);
    boolean existsByUsername(String username);
}
