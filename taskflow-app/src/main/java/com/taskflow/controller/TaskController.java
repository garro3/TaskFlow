package com.taskflow.controller;

import com.taskflow.dto.TaskCreateRequest;
import com.taskflow.dto.TaskResponse;
import com.taskflow.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.UUID;

/**
 * Contrôleur REST minimal pour les opérations sur les tâches.
 *
 * Le contrôleur expose un endpoint POST `/api/tasks` qui délègue la logique
 * métier au `TaskService`. Il tente d'obtenir l'ID de l'utilisateur courant
 * depuis l'en-tête `X-User-Id` ou depuis le champ `authentication.getName()`.
 */
@RestController
@RequestMapping("/api/tasks")
@Validated
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @Valid @RequestBody TaskCreateRequest request
    ) {
        UUID ownerId = resolveOwnerId(userIdHeader);
        TaskResponse resp = taskService.createTask(ownerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    private UUID resolveOwnerId(String header) {
        if (header != null && !header.isBlank()) {
            try {
                return UUID.fromString(header.trim());
            } catch (IllegalArgumentException e) {
                // invalid UUID in header, ignore and try authentication
            }
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName() != null) {
            try {
                return UUID.fromString(auth.getName());
            } catch (IllegalArgumentException e) {
                // authentication name is not a UUID, ignore
            }
        }

        return null;
    }
}
