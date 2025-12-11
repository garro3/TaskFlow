package com.taskflow.controller;

import com.taskflow.dto.UserRegisterRequest;
import com.taskflow.dto.UserResponse;
import com.taskflow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur minimal pour l'authentification (inscription dans un premier temps).
 *
 * L'endpoint `/api/auth/register` reçoit un `UserRegisterRequest` validé
 * puis délègue la création à `UserService`.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint d'inscription.
     * - `@Valid` déclenche la validation JSR-380 sur le DTO
     * - retourne 201 + UserResponse si succès
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegisterRequest req) {
        UserResponse created = userService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
