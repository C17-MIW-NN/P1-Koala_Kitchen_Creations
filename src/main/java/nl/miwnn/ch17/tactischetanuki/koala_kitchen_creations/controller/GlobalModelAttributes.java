package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.controller;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.NavItem;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.NavigationService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;


/**
 * @author jantine van der Schaaf
 * @author Josse Muller
 * Automatically add model parameter for every controller, like navItems needed in Thymeleaf nav bar
 */
@ControllerAdvice
public class GlobalModelAttributes {
    private final NavigationService navigationService;

    public GlobalModelAttributes(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    @ModelAttribute("navItems")
    public List<NavItem> populateNavItems() {
        return navigationService.getNavItems();
    }

}