package com.taskflow.dto;

import com.taskflow.entity.TaskStatus;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class TaskResponse {
    private final UUID id;
    private final String title;
    private final String description;
    private final TaskStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final UUID ownerId;
}
