package com.back.entities.dto;


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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskLabelProjectDto {

    private Long id;

    private Long project_id;
    private String project_name;

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
