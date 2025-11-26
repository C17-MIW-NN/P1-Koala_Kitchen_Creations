package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.dto;

import lombok.*;

/**
 * @author Josse Muller
 * Carries the information who is the recipe's author
 */
@Value
public class UserAuthorDTO {
    private String username;
    private Long id;
}
