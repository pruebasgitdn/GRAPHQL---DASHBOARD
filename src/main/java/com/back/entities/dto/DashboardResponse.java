package com.back.entities.dto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class DashboardResponse {


    private TaskStats taskStats;


    private List<TaskStatusDistributionResponse> tasksPriorityDistribution;
    private List<ProjectStatusDistributionResponse> projectStatusDistribution;



    private List<WorkspaceProductivityData> workspaceProductivityData;

    //taskstrendsdata => 30 dias ant a hoy
    private List<TaskTrendCL> taskTrendsData;

    //upcoming tasks => sig 7 dias
    private List<TaskResponse> upcomingTasks;

    //recent projects => dto del project, tomar los 5 ultimos creados por createdAt
    private List<ProjectResponse> recentProjects;
}
