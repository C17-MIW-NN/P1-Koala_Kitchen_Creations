package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.dto.NewRecipeUserDTO;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.RecipeUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 * Doel methode
 */

@Controller
@RequestMapping("/user")
public class RecipeUserController {
    private final RecipeUserService recipeUserService;

    public RecipeUserController(RecipeUserService recipeUserService) {
        this.recipeUserService = recipeUserService;
    }

    @GetMapping("/all")
    private String showUserOverview(Model datamodel) {
        datamodel.addAttribute("allUsers", recipeUserService.getAllUsers());
        datamodel.addAttribute("formUser", new NewRecipeUserDTO());
        datamodel.addAttribute("formModalHidden", true);

        return "userOverview";
    }

    @PostMapping("/save")
    private String saveOrUpdateUser(@ModelAttribute("formUser") NewRecipeUserDTO userDtoToBeSaved, BindingResult result,
                                    Model datamodel) {
        if (recipeUserService.usernameInUse(userDtoToBeSaved.getUsername())) {
            result.rejectValue("username", "duplicate", "This username is not available");
        }

        if (!userDtoToBeSaved.getPassword().equals(userDtoToBeSaved.getConfirmPassword())) {
            result.rejectValue("password", "no.match", "The passwords do not match");
        }

        if (result.hasErrors()) {
            datamodel.addAttribute("allUsers", recipeUserService.getAllUsers());
            datamodel.addAttribute("formModalHidden", false);
            return "userOverview";
        }

        recipeUserService.save(userDtoToBeSaved);
        return "redirect:/user/all";
    }
}
