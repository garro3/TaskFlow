package com.taskflow.dto;
import com.taskflow.entity.TaskStatus;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class TaskResponseTest {

    @Test
    void builderCreatesTaskResponseWithCorrectFields() {
        UUID id = UUID.randomUUID();
        String title = "Test Task";
        String description = "Test Description";
        TaskStatus status = TaskStatus.IN_PROGRESS;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        UUID ownerId = UUID.randomUUID();

        TaskResponse response = TaskResponse.builder()
                .id(id)
                .title(title)
                .description(description)
                .status(status)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .ownerId(ownerId)
                .build();

        assertEquals(id, response.getId());
        assertEquals(title, response.getTitle());
        assertEquals(description, response.getDescription());
        assertEquals(status, response.getStatus());
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(updatedAt, response.getUpdatedAt());
        assertEquals(ownerId, response.getOwnerId());
    }

    @Test
    void allArgsConstructorSetsFieldsCorrectly() {
        UUID id = UUID.randomUUID();
        String title = "Another Task";
        String description = "Another Description";
        TaskStatus status = TaskStatus.DONE;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        UUID ownerId = UUID.randomUUID();

        TaskResponse response = new TaskResponse(id, title, description, status, createdAt, updatedAt, ownerId);

        assertEquals(id, response.getId());
        assertEquals(title, response.getTitle());
        assertEquals(description, response.getDescription());
        assertEquals(status, response.getStatus());
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(updatedAt, response.getUpdatedAt());
        assertEquals(ownerId, response.getOwnerId());
    }
}