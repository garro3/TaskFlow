package com.taskflow.service;

import com.taskflow.dto.TaskCreateRequest;
import com.taskflow.dto.TaskResponse;
import com.taskflow.entity.Task;
import com.taskflow.entity.TaskStatus;
import com.taskflow.entity.User;
import com.taskflow.repository.TaskRepository;
import com.taskflow.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Squelettes de tests unitaires pour `TaskService`.
 *
 * Remarque : adaptes les noms de méthodes si ton `TaskService` diffère.
 * Ces tests démontrent les cas usuels : création réussie et refus
 * d'accès si l'utilisateur n'est pas le propriétaire.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    // InjectMocks cherche une classe TaskService dans le classpath. Si tu utilises
    // un nom différent, adapte la déclaration ci‑dessous ou renomme ta classe.
    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_success() {
        // Prépare les données d'entrée
        UUID ownerId = UUID.randomUUID();
        TaskCreateRequest req = new TaskCreateRequest();
        req.setTitle("New task");
        req.setDescription("Do something");

        // Simule la présence de l'utilisateur propriétaire
        User owner = new User();
        owner.setId(ownerId);
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));

        // Capture la Task passée au repository.save
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        when(taskRepository.save(captor.capture())).thenAnswer(inv -> {
            Task t = captor.getValue();
            t.setId(UUID.randomUUID());
            t.setCreatedAt(LocalDateTime.now());
            return t;
        });

        // Appel de la méthode (nom hypothétique : createTask)
        TaskResponse resp = taskService.createTask(ownerId, req);

        assertNotNull(resp.getId());
        assertEquals(req.getTitle(), resp.getTitle());
        assertEquals(req.getDescription(), resp.getDescription());
        assertEquals(TaskStatus.TODO, resp.getStatus());

        // Vérifie qu'on a utilisé le repository
        verify(userRepository).findById(ownerId);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void updateTask_forbiddenIfNotOwner() {
        UUID ownerId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();

        // La tâche existe mais appartient à `ownerId`
        Task stored = new Task();
        stored.setId(taskId);
        User owner = new User(); owner.setId(ownerId);
        stored.setOwner(owner);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(stored));

        // Prépare requête de mise à jour
        TaskCreateRequest req = new TaskCreateRequest();
        req.setTitle("Updated");

        // Appel d'une méthode hypothétique updateTask(taskId, requestingUserId, req)
        // Ici, on s'attend à une exception si l'utilisateur n'est pas propriétaire
        assertThrows(ResponseStatusException.class, () ->
                taskService.updateTask(taskId, otherUserId, req)
        );

        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any());
    }
}
