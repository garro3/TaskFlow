package com.taskflow.entity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class RoleTest {

    @Test
    void testRoleValues() {
        Role[] roles = Role.values();
        assertEquals(2, roles.length);
        assertTrue(containsRole(roles, Role.ROLE_USER));
        assertTrue(containsRole(roles, Role.ROLE_ADMIN));
    }

    @Test
    void testValueOf() {
        assertEquals(Role.ROLE_USER, Role.valueOf("ROLE_USER"));
        assertEquals(Role.ROLE_ADMIN, Role.valueOf("ROLE_ADMIN"));
    }

    private boolean containsRole(Role[] roles, Role role) {
        for (Role r : roles) {
            if (r == role) return true;
        }
        return false;
    }
}