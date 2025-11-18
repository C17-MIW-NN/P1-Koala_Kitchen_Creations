package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Jantine van der Schaaf
 * Doel methode
 */
public interface ImageRepository extends JpaRepository<Image, Long>{
    Optional<Image> findByFileName(String fileName);
    boolean existsByFileName(String fileName);
}

