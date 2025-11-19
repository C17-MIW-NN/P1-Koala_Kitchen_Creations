package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Category;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
