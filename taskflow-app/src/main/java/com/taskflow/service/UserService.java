package com.taskflow.service;

import com.taskflow.dto.UserRegisterRequest;
import com.taskflow.dto.UserResponse;
import com.taskflow.entity.Role;
import com.taskflow.entity.User;
import com.taskflow.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Service responsable de la logique métier liée aux utilisateurs.
 *
 * Responsabilités principales :
 * - vérifier l'unicité du username/email
 * - encoder le mot de passe
 * - assigner le rôle par défaut
 * - sauvegarder l'utilisateur
 * - mapper l'entité vers un DTO de sortie
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Inscrit un nouvel utilisateur.
     *
     * Étapes :
     * 1) vérifier si le username/email existent déjà -> 400
     * 2) encoder le mot de passe avec BCrypt
     * 3) assigner le rôle ROLE_USER par défaut
     * 4) sauvegarder et renvoyer un UserResponse (sans password)
     */
    public UserResponse register(UserRegisterRequest req) {
        // 1) checks d'unicité
        if (userRepository.existsByUsername(req.getUsername())) {
            // ResponseStatusException permet de renvoyer un statut HTTP facilement
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        // 2) créer l'entité et hacher le mot de passe
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        // NE JAMAIS stocker le mot de passe en clair
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        // 3) assigner le rôle par défaut
        user.getRoles().add(Role.ROLE_USER);

        // 4) sauvegarder en base
        User saved = userRepository.save(user);

        // 5) mapper vers DTO de sortie (ne contient pas le mot de passe)
        return mapToResponse(saved);
    }

    // Méthode simple de mapping (on peut remplacer par MapStruct si on veut)
    private UserResponse mapToResponse(User u) {
        return new UserResponse(u.getId(), u.getUsername(), u.getEmail(), u.getRoles());
    }
}
