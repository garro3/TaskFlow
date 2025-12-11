package com.taskflow.service;

import com.taskflow.dto.TaskCreateRequest;
import com.taskflow.dto.TaskResponse;
import com.taskflow.entity.Task;
import com.taskflow.entity.TaskStatus;
import com.taskflow.entity.User;
import com.taskflow.repository.TaskRepository;
import com.taskflow.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

/**
 * Service pour la gestion des tâches (Task).
 *
 * Ce service est volontairement minimal : il expose des méthodes utilisées
 * par les tests et par le contrôleur. Il contient la logique métier de base
 * (création, mise à jour) et les vérifications d'accès (owner).
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    /**
     * Crée une tâche pour l'utilisateur `ownerId`.
     *
     * - Vérifie que l'utilisateur existe.
    * - Initialise la Task (statut initial défini par l'enum TaskStatus, timestamps gérés par l'entité).
     * - Sauvegarde et retourne un DTO `TaskResponse`.
     */
    public TaskResponse createTask(UUID ownerId, TaskCreateRequest req) {
        if (ownerId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ownerId is required");
        }

        Optional<User> maybeOwner = userRepository.findById(ownerId);
        if (maybeOwner.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found");
        }

        User owner = maybeOwner.get();

        Task t = new Task();
        t.setTitle(req.getTitle());
        t.setDescription(req.getDescription());
        t.setStatus(TaskStatus.TODO);
        t.setOwner(owner);

        Task saved = taskRepository.save(t);

        return mapToResponse(saved);
    }

    /**
     * Met à jour une tâche en vérifiant que `requestingUserId` est bien le owner.
     */
    public TaskResponse updateTask(UUID taskId, UUID requestingUserId, TaskCreateRequest req) {
        Task t = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        // Vérification d'appartenance : seul le propriétaire peut modifier
        if (t.getOwner() == null || !t.getOwner().getId().equals(requestingUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed");
        }

        if (req.getTitle() != null) t.setTitle(req.getTitle());
        if (req.getDescription() != null) t.setDescription(req.getDescription());

        Task saved = taskRepository.save(t);
        return mapToResponse(saved);
    }

    private TaskResponse mapToResponse(Task t) {
        return new TaskResponse(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.getStatus(),
                t.getCreatedAt(),
                t.getUpdatedAt(),
                t.getOwner() != null ? t.getOwner().getId() : null
        );
    }
}
