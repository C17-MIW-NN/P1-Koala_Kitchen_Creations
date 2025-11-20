package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 * A cagtegory that could contain multiple recipes
 */
@Getter
@Setter
@Entity
public class Category {
    @Id
    @GeneratedValue
    private Long categoryId;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy="categories")
    private Set<Recipe> recipes;

    public Category() {
        this.recipes = new HashSet<>();
    }
    public Category(String name) {
        this();
        this.name = name;
    }
    public boolean equals(Category other) {
        return categoryId.equals(other.categoryId);
    }

    @Override
    public int hashCode() {
        return categoryId.hashCode();
    }
}
