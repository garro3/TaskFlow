package com.taskflow.entity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.UUID;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;




class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("hashedpassword");
        user.setRoles(new HashSet<>(Set.of(Role.ROLE_USER)));
    }

    @Test
    void testUserFields() {
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("hashedpassword", user.getPassword());
        assertTrue(user.getRoles().contains(Role.ROLE_USER));
    }

    @Test
    void testOnCreateSetsDates() {
        user.onCreate();
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertEquals(user.getCreatedAt(), user.getUpdatedAt());
    }

    @Test
    void testOnUpdateSetsUpdatedAt() {
        user.onCreate();
        LocalDateTime created = user.getCreatedAt();
        LocalDateTime updatedBefore = user.getUpdatedAt();
        user.onUpdate();
        assertEquals(created, user.getCreatedAt());
        assertTrue(user.getUpdatedAt().isAfter(updatedBefore) || user.getUpdatedAt().isEqual(updatedBefore));
    }

    @Test
    void testRolesCanBeModified() {
        user.getRoles().add(Role.ROLE_ADMIN);
        assertTrue(user.getRoles().contains(Role.ROLE_ADMIN));
    }

    @Test
    void testSettersAndGetters() {
        UUID uuid = UUID.randomUUID();
        user.setId(uuid);
        assertEquals(uuid, user.getId());

        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }
}