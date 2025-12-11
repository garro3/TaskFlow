package com.taskflow.entity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class TaskStatusTest {

    @Test
    void testEnumValues() {
        TaskStatus[] statuses = TaskStatus.values();
        assertEquals(3, statuses.length);
        assertArrayEquals(
            new TaskStatus[]{TaskStatus.TODO, TaskStatus.IN_PROGRESS, TaskStatus.DONE},
            statuses
        );
    }

    @Test
    void testValueOf() {
        assertEquals(TaskStatus.TODO, TaskStatus.valueOf("TODO"));
        assertEquals(TaskStatus.IN_PROGRESS, TaskStatus.valueOf("IN_PROGRESS"));
        assertEquals(TaskStatus.DONE, TaskStatus.valueOf("DONE"));
    }

    @Test
    void testInvalidValueOfThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            TaskStatus.valueOf("INVALID");
        });
    }
}