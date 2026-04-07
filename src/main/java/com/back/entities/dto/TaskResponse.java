package com.back.entities.dto;


import com.back.entities.Project;
import com.back.enums.TaskPriority;
import com.back.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private Long id;

    private String title;

    private String description;

    private ProjectResponse project;

    private TaskStatus status;

    private TaskPriority priority;

}
