package com.back.entities.dto;

import com.back.enums.TaskPriority;
import com.back.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditTaskInput {

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;
}
