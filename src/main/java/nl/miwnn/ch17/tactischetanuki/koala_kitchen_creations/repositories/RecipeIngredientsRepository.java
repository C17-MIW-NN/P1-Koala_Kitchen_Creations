package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories;


import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeIngredients;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientsRepository extends JpaRepository<RecipeIngredients, Long>  {
}
