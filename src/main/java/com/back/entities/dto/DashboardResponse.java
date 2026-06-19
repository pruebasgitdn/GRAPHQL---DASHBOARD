package com.back.entities.dto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class DashboardResponse {

    //stats
    private Long totalProjects;
    private Long totalProjectInProgress;
    private TaskStats taskStats;


    //private List<WorkspaceProductivityData> workspaceProductivityData;
    private List<TaskPriorityDistributionResponse> tasksPriorityDistribution;
    private List<TaskStatusDistributionResponse> tasksStatusDistribution;


    //upcoming tasks =>
    //recent projects => dto del project, tomar los 5 ultimos creados por createdAt
    //taskstrendsdata =>

}
