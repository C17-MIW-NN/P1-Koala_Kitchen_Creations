package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.dto;

import lombok.*;

/**
 * @author Josse Muller
 * Transfer ingredient data to and from view
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeIngredientDto {

    private Long id;
    private String name;
    private double quantity;
    private String unit;

}
