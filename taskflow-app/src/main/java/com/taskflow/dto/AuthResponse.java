package com.taskflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO retourné après authentification réussie : contient le token JWT.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken; // JWT
    private String tokenType = "Bearer";
    private Instant expiresAt;  // date d'expiration (optionnelle)
}
