package com.back.entities.dto;

import com.back.entities.Task;
import com.back.entities.mappers.UserMapper;
import com.back.enums.TaskLabelType;
import com.back.enums.TaskPriority;
import com.back.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyTasksDto {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime completedAt;
    private Boolean isArchived;
    private Double actualHours;
    private Double estimatedHours;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long project_id;
    private UUID workspace_id;

    private UserResponse owner;

    private List<TaskLabelType> labels;
    private List<SubTaskResponse> subTasks;
}
