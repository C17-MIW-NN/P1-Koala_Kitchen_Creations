package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.dto.NewRecipeUserDTO;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Recipe;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeUser;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.RecipeUserRepository;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service.mappers.RecipeUserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 */

@Getter
@Setter
@Service
public class RecipeUserService implements UserDetailsService {
    private final RecipeUserRepository recipeUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecipeRepository recipeRepository;


    public RecipeUserService(RecipeUserRepository recipeUserRepository, PasswordEncoder passwordEncoder,
                             RecipeRepository recipeRepository) {
        this.recipeUserRepository = recipeUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return recipeUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " was not found."));
    }

    public void saveUser(RecipeUser recipeUser) {
        recipeUser.setPassword(passwordEncoder.encode(recipeUser.getPassword()));
        recipeUserRepository.save(recipeUser);
    }

    public List<RecipeUser> getAllUsers() {
        return recipeUserRepository.findAll();
    }

    public boolean usernameInUse(String username) {
        return recipeUserRepository.existsByUsername(username);
    }

    public void save(NewRecipeUserDTO userDtoToBeSaved) {
        saveUser(RecipeUserMapper.fromDTO(userDtoToBeSaved));
    }

    @Transactional
    public RecipeUser getUserWithFavorites(String username) {
        RecipeUser user = recipeUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.getFavorites().size();

        return user;
    }

    @Transactional
    public void addFavorite(String username, Long recipeId) {
        RecipeUser user = getUserWithFavorites(username);
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();
        user.getFavorites().add(recipe);
    }

    @Transactional
    public void removeFavorite(String username, Long recipeId) {
        RecipeUser user = getUserWithFavorites(username);
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();
        user.getFavorites().remove(recipe);
    }
}
