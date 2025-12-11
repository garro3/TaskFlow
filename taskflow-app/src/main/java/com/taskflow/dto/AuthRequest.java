package com.taskflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO utilisé pour la connexion (login).
 * Contient généralement username/email + password.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    @NotBlank
    private String username; // ou email selon l'implémentation

    @NotBlank
    private String password;
}
