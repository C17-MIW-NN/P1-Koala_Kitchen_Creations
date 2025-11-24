package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service;

import lombok.Getter;
import lombok.Setter;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.dto.NewRecipeUserDTO;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.RecipeUser;
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
    private final RecipeUserRepository RecipeUserRepository;
    private final PasswordEncoder passwordEncoder;


    public RecipeUserService(RecipeUserRepository recipeUserRepository, PasswordEncoder passwordEncoder) {
        RecipeUserRepository = recipeUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return RecipeUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " was not found."));
    }

    public void saveUser(RecipeUser recipeUser) {
        recipeUser.setPassword(passwordEncoder.encode(recipeUser.getPassword()));
        RecipeUserRepository.save(recipeUser);
    }

    public List<RecipeUser> getAllUsers() {
        return RecipeUserRepository.findAll();
    }

    public boolean usernameInUse(String username) {
        return RecipeUserRepository.existsByUsername(username);
    }

    public void save(NewRecipeUserDTO userDtoToBeSaved) {
        saveUser(RecipeUserMapper.fromDTO(userDtoToBeSaved));
    }
}
