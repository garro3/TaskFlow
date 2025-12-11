package com.taskflow.dto;

import com.taskflow.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

/**
 * DTO renvoyé au client après création/consultation d'un utilisateur.
 * Ne contient jamais le champ `password` pour des raisons de sécurité.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;               // Identifiant public
    private String username;       // Nom d'utilisateur affichable
    private String email;          // Email de contact
    private Set<Role> roles;       // Rôles attribués (p.ex. ROLE_USER)
}
