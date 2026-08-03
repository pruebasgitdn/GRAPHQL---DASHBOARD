package com.back.entities.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TaskStats {
    private Long totalTasks;
    private Long totalTodoTasks;
    private Long totalInProgressTasks;
    private Long totalDoneTasks;
    private Long totalProjects;
    private Long totalProjectInProgress;
}
