package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Josse Muller
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
