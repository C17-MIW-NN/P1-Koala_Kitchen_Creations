package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 * Configures the security for the library application
 */

@Configuration
@EnableWebSecurity
public class KoalaKitchenCreationsSecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests((request) -> request
                        .requestMatchers("/", "/recipe/all").permitAll()
                        .requestMatchers("/recipe/detail/*", "/category/all").permitAll()
                        .requestMatchers("/css/**", "/webjars/**", "image/**", "images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .logout((logout) -> logout.logoutSuccessUrl("/"))
        ;
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
