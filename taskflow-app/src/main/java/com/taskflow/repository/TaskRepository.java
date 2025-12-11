package com.taskflow.repository;

import com.taskflow.entity.Task;
import com.taskflow.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

// Définition de l'interface du repository pour l'entité Task, utilisant JpaRepository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    // Récupère une page de tâches appartenant à un utilisateur donné (owner.id)
    Page<Task> findByOwner_Id(UUID ownerId, Pageable pageable);

    // Récupère une page de tâches appartenant à un utilisateur donné (owner.id) et ayant un statut spécifique
    Page<Task> findByOwner_IdAndStatus(UUID ownerId, TaskStatus status, Pageable pageable);

}
