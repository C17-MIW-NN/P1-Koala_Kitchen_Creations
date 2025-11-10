package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Josse Muller
 * Load recipe data and show recipes
 */
@Controller
public class RecipeController {
    @GetMapping({"/recipe/all", "/"})
    public String showRecipeOverview() {
        return "recipeList";
    }
}
