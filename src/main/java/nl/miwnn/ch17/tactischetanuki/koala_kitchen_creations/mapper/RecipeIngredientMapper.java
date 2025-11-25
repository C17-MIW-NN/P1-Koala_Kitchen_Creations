package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.mapper;

import lombok.RequiredArgsConstructor;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.dto.RecipeIngredientDto;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeIngredients;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.IngredientRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.IngredientService;
import org.springframework.stereotype.Component;

/**
 * @author Josse Muller
 * Map between RecipeIngredient and the related DTO.
 * Retrieving the linked ingredient should be done in the IngredientService
 */
@RequiredArgsConstructor
@Component
public class RecipeIngredientMapper {
    private final IngredientService ingredientService;
    public RecipeIngredientDto toDto(RecipeIngredients entity) {
        return new RecipeIngredientDto(
                entity.getRecipeIngredientsId(),
                entity.getIngredient().getName(),
                entity.getQuantity());
    }
    public RecipeIngredients toEntity(RecipeIngredientDto dto) {
        return new RecipeIngredients(
                dto.getId(),
                ingredientService.findOrCreateByName(dto.getName()),
                dto.getQuantity());
    }
}
