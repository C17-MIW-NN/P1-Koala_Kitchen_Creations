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

    private int stepNumber;

    @Column(columnDefinition = "TEXT")
    private String stepDescription;

    @ManyToOne
    private Recipe recipe;

    public RecipeStep(Recipe recipe, String stepDescription, int stepNumber) {
        this.recipe = recipe;
        this.stepDescription = stepDescription;
        this.stepNumber = stepNumber;
    }

    public RecipeStep() {
    }
}
