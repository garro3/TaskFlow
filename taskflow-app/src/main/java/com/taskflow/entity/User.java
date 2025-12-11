package com.taskflow.entity; // Définit le package où se trouve la classe

import jakarta.persistence.*; // Importe les annotations JPA pour la persistance
import lombok.AllArgsConstructor; // Génère un constructeur avec tous les arguments
import lombok.Data; // Génère getters, setters, equals, hashCode, toString
import lombok.NoArgsConstructor; // Génère un constructeur sans argument
import java.time.LocalDateTime; // Pour gérer les dates et heures sans fuseau horaire
import java.util.UUID; // Pour générer des identifiants uniques universels
import java.util.Set;
import java.util.HashSet;
import jakarta.persistence.FetchType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity // Indique que cette classe est une entité JPA (table de base de données)
@Table(name = "users") // Spécifie le nom de la table associée à cette entité
@Data // Lombok : génère automatiquement getters, setters, etc.
@NoArgsConstructor // Lombok : génère un constructeur sans argument
@AllArgsConstructor // Lombok : génère un constructeur avec tous les arguments

public class User { // Déclaration de la classe User

    @Id // Indique que ce champ est la clé primaire
    @GeneratedValue(strategy = GenerationType.UUID) // Génère automatiquement un UUID pour chaque nouvel utilisateur
    private UUID id; // Identifiant unique de l'utilisateur

    @Column(nullable = false, unique = true) // Champ obligatoire et unique
    private String username; // Nom d'utilisateur

    @Column(nullable = false, unique = true) // Champ obligatoire et unique
    private String email; // Adresse email de l'utilisateur

    @Column(nullable = false) // Champ obligatoire
    private String password; // Mot de passe de l'utilisateur (à stocker haché en pratique)

    @ElementCollection(fetch = FetchType.EAGER) // Indique que ce champ est une collection d'éléments simples (ici, des rôles), chargée immédiatement avec l'entité User
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id")) // Spécifie la table d'association "user_roles" et la colonne de jointure "user_id"
    @Enumerated(EnumType.STRING) // Stocke les valeurs de l'énumération Role sous forme de chaînes de caractères dans la base
    private Set<Role> roles = new HashSet<>(); // Ensemble des rôles attribués à l'utilisateur, initialisé vide par défaut

    @Column(name = "created_at", nullable = false, updatable = false) // Colonne nommée "created_at" dans la base
    private LocalDateTime createdAt; // Date de création de l'utilisateur

    @Column(name = "updated_at") // Colonne nommée "updated_at" dans la base
    private LocalDateTime updatedAt; // Date de dernière modification

    @PrePersist // Méthode appelée avant l'insertion en base (création)
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now; // Initialise la date de création
        updatedAt = now; // Initialise aussi la date de modification
    }

    @PreUpdate // Méthode appelée avant chaque mise à jour en base
    protected void onUpdate() {
        updatedAt = LocalDateTime.now(); // Met à jour la date de modification
    }
}