package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jantine van der Schaaf
 * The steps form the preparation method of a dish
 */
@Getter
@Setter
@Entity
public class RecipeStep {

    @Id
    @GeneratedValue
    private Long stepId;

    @Column(columnDefinition = "TEXT")
    private String stepDescription;

    @ManyToOne
    private Recipe recipe;

    public RecipeStep(Recipe recipe, String stepDescription) {
        this(stepDescription);
        this.recipe = recipe;
    }

    public RecipeStep(String stepDescription) {
        this();
        this.stepDescription = stepDescription;
    }

    public RecipeStep() {
    }
}
