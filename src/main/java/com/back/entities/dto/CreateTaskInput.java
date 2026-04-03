package com.back.entities.dto;

import com.back.enums.TaskPriority;
import com.back.enums.TaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CreateTaskInput {

    @NotNull(message = "Indica el titulo de la tarea")
    private String title;

    @NotNull(message = "La tarea debe ir asociado a un proyecto")
    private UUID projectId;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;



}
