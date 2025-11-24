package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.mappers;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.dto.NewRecipeUserDTO;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeUser;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 * Converts NewRecipeUserDTO Objects into RecipeUsers
 */
public class RecipeUserMapper {
    public static RecipeUser fromDTO(NewRecipeUserDTO newRecipeUserDTO) {
        RecipeUser recipeUser = new RecipeUser();

        recipeUser.setUsername(newRecipeUserDTO.getUsername());
        recipeUser.setPassword(newRecipeUserDTO.getPassword());

        return recipeUser;
    }
}
