package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 */
@Getter
@Setter
public class NewRecipeUserDTO {
    private String username;
    private String password;
    private String confirmPassword;
}
