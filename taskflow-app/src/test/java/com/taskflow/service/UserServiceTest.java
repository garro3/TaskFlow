package com.taskflow.service;

import com.taskflow.dto.UserRegisterRequest;
import com.taskflow.dto.UserResponse;
import com.taskflow.entity.Role;
import com.taskflow.entity.User;
import com.taskflow.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour {@link UserService}.
 * On se concentre sur la méthode `register` :
 * - cas succès
 * - cas username déjà existant
 * - cas email déjà existant
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    void register_success() {
        // Prépare la requête d'inscription
        UserRegisterRequest req = new UserRegisterRequest("alice", "a@ex.com", "secret123");

        // Simule l'absence d'utilisateur existant
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.existsByEmail("a@ex.com")).thenReturn(false);

        // Simule l'encodage du mot de passe
        when(passwordEncoder.encode("secret123")).thenReturn("$2a$hashed");

        // Capture l'entité passée à save pour vérifier son contenu
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(captor.capture())).thenAnswer(inv -> {
            User u = captor.getValue();
            // Simule la persistence en assignant un id
            u.setId(UUID.randomUUID());
            return u;
        });

        // Appel de la méthode testée
        UserResponse resp = userService.register(req);

        // Vérifications
        assertNotNull(resp.getId());
        assertEquals("alice", resp.getUsername());
        assertEquals("a@ex.com", resp.getEmail());
        assertTrue(resp.getRoles().contains(Role.ROLE_USER));

        // Vérifie que le mot de passe sauvegardé est le hash retourné par PasswordEncoder
        User saved = captor.getValue();
        assertEquals("$2a$hashed", saved.getPassword());

        verify(userRepository).existsByUsername("alice");
        verify(userRepository).existsByEmail("a@ex.com");
        verify(passwordEncoder).encode("secret123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_conflictUsername() {
        UserRegisterRequest req = new UserRegisterRequest("bob", "b@ex.com", "pwd12345");
        when(userRepository.existsByUsername("bob")).thenReturn(true);

        // Doit lancer une ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> userService.register(req));

        verify(userRepository).existsByUsername("bob");
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_conflictEmail() {
        UserRegisterRequest req = new UserRegisterRequest("carol", "c@ex.com", "pwd12345");
        when(userRepository.existsByUsername("carol")).thenReturn(false);
        when(userRepository.existsByEmail("c@ex.com")).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> userService.register(req));

        verify(userRepository).existsByUsername("carol");
        verify(userRepository).existsByEmail("c@ex.com");
        verify(userRepository, never()).save(any());
    }
}
