package com.taskflow.repository; // Déclare le package du fichier

import com.taskflow.entity.User; // Importe l'entité User
import org.springframework.data.jpa.repository.JpaRepository; // Importe JpaRepository pour l'accès aux données
import java.util.UUID; // Importe la classe UUID
import java.util.Optional; // Importe la classe Optional

// Déclare l'interface UserRepository qui hérite de JpaRepository pour l'entité User avec une clé primaire de type UUID
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username); // Méthode pour trouver un utilisateur par son nom d'utilisateur

    Optional<User> findByEmail(String email); // Méthode pour trouver un utilisateur par son email

    boolean existsByUsername(String username); // Vérifie si un utilisateur existe avec ce nom d'utilisateur

    boolean existsByEmail(String email); // Vérifie si un utilisateur existe avec cet email
    
}
