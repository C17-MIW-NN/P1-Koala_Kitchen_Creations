package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Josse Muller
 * An ingredient with name preferred unit, that can be reused in various recipes
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ingredient {

    @GeneratedValue
    @Id
    private Long id;

    @Column(unique = true)
    private String name;

    private String defaultUnit;

    public Ingredient(String name, String defaultUnit) {
        this();
        this.name = name;
        this.defaultUnit = defaultUnit;
    }
}
