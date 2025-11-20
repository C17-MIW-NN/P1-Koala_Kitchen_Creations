package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Category;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 * Used to create and find categories from one list
 */
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Set<Category> findOrCreateByNames(List<String> categoryNames) {
        categoryNames = categoryNames.stream().map(String::trim).toList();
        Set<Category> selectedCategories = new HashSet<>();
        List<Category> existingCategories = categoryRepository.findAllByNameIn(categoryNames);
        selectedCategories.addAll(existingCategories);
        Set<String> existingNames = existingCategories.stream().map(Category::getName).collect(Collectors.toSet());

        Set<String> newCategoryNames = new HashSet<>(categoryNames);
        newCategoryNames.removeAll(existingNames);
        for (String name : newCategoryNames) {
            name = name.trim();
            selectedCategories.add(categoryRepository.save(new Category(name)));
        }

        return selectedCategories;
    }
}
