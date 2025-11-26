package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.NavItem;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 * Provides the list of links in the navbar
 */
@Service
public class NavigationService {
    private static final List<NavItem> navItems = List.of(
            new NavItem("Recipes", "/recipe/all", false),
            new NavItem("Categories", "/category/all", false),
            new NavItem("Favorites", "/recipe/favorites", true),
            new NavItem("Users", "/user/all", true)
    );
    public List<NavItem> getNavItems() {
        return navItems;
    }
}
