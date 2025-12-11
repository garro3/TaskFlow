package com.taskflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO reçu depuis le client lors de l'inscription.
 *
 * - Contient uniquement les champs attendus en entrée (username, email, password).
 * - Les annotations de validation JSR-380 sont appliquées ici pour vérifier
 *   les contraintes avant d'entrer dans la couche service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {

    // Le nom d'utilisateur ne doit pas être vide et doit avoir une longueur raisonnable
    @NotBlank(message = "username is required")
    @Size(min = 3, max = 50, message = "username must be between 3 and 50 chars")
    private String username;

    // L'email doit être une adresse valide
    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    @Size(max = 100)
    private String email;

    // Le mot de passe brut envoyé par le client (sera hashé côté service)
    @NotBlank(message = "password is required")
    @Size(min = 8, max = 100, message = "password must be at least 8 chars")
    private String password;
}

