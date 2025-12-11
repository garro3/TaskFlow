package com.taskflow.entity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;





class TaskTest {

    private User owner;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(UUID.randomUUID());
        owner.setUsername("testuser");
    }

    @Test
    void testNoArgsConstructor() {
        Task task = new Task();
        assertNull(task.getId());
        assertNull(task.getTitle());
        assertNull(task.getDescription());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertNull(task.getCreatedAt());
        assertNull(task.getUpdatedAt());
        assertNull(task.getOwner());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        String title = "Test Task";
        String description = "Description";
        TaskStatus status = TaskStatus.IN_PROGRESS;
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();
        Task task = new Task(id, title, description, status, createdAt, updatedAt, owner);

        assertEquals(id, task.getId());
        assertEquals(title, task.getTitle());
        assertEquals(description, task.getDescription());
        assertEquals(status, task.getStatus());
        assertEquals(createdAt, task.getCreatedAt());
        assertEquals(updatedAt, task.getUpdatedAt());
        assertEquals(owner, task.getOwner());
    }

    @Test
    void testOnCreateSetsFields() {
        Task task = new Task();
        task.setTitle("Title");
        task.setDescription("Desc");
        task.setOwner(owner);

        task.onCreate();

        assertNotNull(task.getId());
        assertNotNull(task.getCreatedAt());
        assertNotNull(task.getUpdatedAt());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertEquals(task.getCreatedAt(), task.getUpdatedAt());
    }

    @Test
    void testOnUpdateSetsUpdatedAt() {
        Task task = new Task();
        task.setTitle("Title");
        task.setDescription("Desc");
        task.setOwner(owner);
        task.onCreate();

        LocalDateTime beforeUpdate = task.getUpdatedAt();
        task.onUpdate();
        assertTrue(task.getUpdatedAt().isAfter(beforeUpdate));
    }

    @Test
    void testStatusDefaultValue() {
        Task task = new Task();
        assertEquals(TaskStatus.TODO, task.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        Task task = new Task();
        UUID id = UUID.randomUUID();
        String title = "Title";
        String description = "Desc";
        TaskStatus status = TaskStatus.DONE;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        task.setId(id);
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setCreatedAt(createdAt);
        task.setUpdatedAt(updatedAt);
        task.setOwner(owner);

        assertEquals(id, task.getId());
        assertEquals(title, task.getTitle());
        assertEquals(description, task.getDescription());
        assertEquals(status, task.getStatus());
        assertEquals(createdAt, task.getCreatedAt());
        assertEquals(updatedAt, task.getUpdatedAt());
        assertEquals(owner, task.getOwner());
    }
}