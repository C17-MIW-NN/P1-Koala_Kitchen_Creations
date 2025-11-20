package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model;

import lombok.Value;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 * A name and link, to be displayed in the navbar
 */
@Value
public class NavItem {
    public String name;
    public String href;
}
