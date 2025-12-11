package com.taskflow.entity; // Déclaration du package

import jakarta.persistence.*; // Import des annotations JPA
import lombok.AllArgsConstructor; // Import de l'annotation Lombok pour le constructeur avec tous les arguments
import lombok.Data; // Import de l'annotation Lombok pour les getters/setters/toString/hashCode/equals
import lombok.NoArgsConstructor; // Import de l'annotation Lombok pour le constructeur sans argument

import java.time.LocalDateTime; // Import de la classe pour gérer les dates et heures
import java.util.UUID; // Import de la classe pour les identifiants uniques

@Entity // Indique que cette classe est une entité JPA
@Table(name = "tasks") // Spécifie le nom de la table dans la base de données
@Data // Génère automatiquement les méthodes getters/setters/toString/hashCode/equals
@NoArgsConstructor // Génère un constructeur sans argument
@AllArgsConstructor // Génère un constructeur avec tous les arguments

public class Task { // Déclaration de la classe Task

    @Id // Indique la clé primaire
    @GeneratedValue(strategy = GenerationType.UUID) // Génère automatiquement un UUID pour l'id
    private UUID id; // Identifiant unique de la tâche

    @Column(nullable = false) // Colonne non nulle pour le titre
    private String title; // Titre de la tâche

    @Column(columnDefinition = "text") // Colonne de type texte pour la description
    private String description; // Description de la tâche

    @Enumerated(EnumType.STRING) // Stocke l'énumération comme une chaîne
    @Column(nullable = false) // Colonne non nulle pour le statut
    private TaskStatus status = TaskStatus.TODO; // Statut de la tâche, valeur par défaut

    @Column(name = "created_at", nullable = false, updatable = false) // Colonne non modifiable pour la date de création
    private LocalDateTime createdAt; // Date de création de la tâche

    @Column(name = "updated_at") // Colonne pour la date de mise à jour
    private LocalDateTime updatedAt; // Date de dernière mise à jour de la tâche

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // Relation plusieurs-à-un avec User, chargement paresseux
    @JoinColumn(name = "owner_id", nullable = false) // Clé étrangère vers l'utilisateur propriétaire
    private User owner; // Propriétaire de la tâche

    @PrePersist // Méthode appelée avant la persistance de l'entité
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        LocalDateTime now = LocalDateTime.now();
        createdAt = now; // Initialise la date de création
        updatedAt = now; // Initialise la date de mise à jour
    }

    @PreUpdate // Méthode appelée avant la mise à jour de l'entité
    protected void onUpdate() {
        updatedAt = LocalDateTime.now(); // Met à jour la date de mise à jour
    }
}
