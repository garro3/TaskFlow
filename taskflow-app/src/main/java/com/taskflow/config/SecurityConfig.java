package com.taskflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration minimale exposant un bean `PasswordEncoder`.
 *
 * Ce bean sera injecté dans `UserService` pour hacher les mots de passe
 * avant de les persister en base.
 */
@Configuration
public class SecurityConfig {

    /**
     * Crée un BCryptPasswordEncoder avec strength 10 (bonne sécurité par défaut).
     * Le bean est partagé dans l'application via injection.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
