package com.taskflow.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class TaskCreateRequestTest {
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validTaskCreateRequest_shouldHaveNoViolations() {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("Valid Title");
        request.setDescription("Valid description");

        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void blankTitle_shouldHaveViolations() {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("");
        request.setDescription("Some description");

        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }

    @Test
    void nullTitle_shouldHaveViolations() {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle(null);
        request.setDescription("Some description");

        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }

    @Test
    void titleTooLong_shouldHaveViolations() {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("a".repeat(201));
        request.setDescription("Some description");

        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }

    @Test
    void descriptionTooLong_shouldHaveViolations() {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("Valid Title");
        request.setDescription("a".repeat(5001));

        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void nullDescription_shouldHaveNoViolations() {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("Valid Title");
        request.setDescription(null);

        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }
}