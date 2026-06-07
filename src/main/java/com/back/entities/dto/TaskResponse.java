package com.back.entities.dto;


import com.back.entities.Project;
import com.back.enums.TaskLabelType;
import com.back.enums.TaskPriority;
import com.back.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private Boolean isArchived;

    private UserResponse owner;

    private Double estimatedHours;

    private Double actualHours;

    private LocalDateTime completedAt;
    private LocalDateTime createdAt;

    private LocalDate dueDate;

    private List<SubTaskResponse> subTasks;

    private List<TaskLabelType> labels;

}
