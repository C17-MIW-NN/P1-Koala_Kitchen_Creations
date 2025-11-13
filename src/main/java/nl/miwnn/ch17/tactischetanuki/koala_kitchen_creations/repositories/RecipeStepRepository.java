package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories;


import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {
    List<RecipeStep> findByRecipeIdOrderByStepNumber(Long recipeId);
}
