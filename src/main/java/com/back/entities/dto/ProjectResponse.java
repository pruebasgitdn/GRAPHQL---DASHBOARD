package com.back.entities.dto;


import com.back.entities.Workspace;
import com.back.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class  ProjectResponse {

    private Long id;
    private String name;
    private String description;
    
    private WorkspaceResponse workspace;

    private Long tasksCount;

    private LocalDate startDate;
    private LocalDate dueDate;
    private ProjectStatus status;
    private UserResponse owner;
    private Boolean isArchived;

    private List<TaskResponse> tasks;

}