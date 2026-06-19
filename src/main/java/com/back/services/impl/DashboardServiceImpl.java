package com.back.services.impl;

import com.back.entities.dto.*;
import com.back.entities.mappers.WorkspaceMapper;
import com.back.enums.ProjectStatus;
import com.back.enums.TaskPriority;
import com.back.enums.TaskStatus;
import com.back.repositories.ProjectRepository;
import com.back.repositories.TasksRepository;
import com.back.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final TasksRepository tasksRepository;
    private final ProjectRepository projectRepository;
    private final WorkspaceMapper workspaceMapper;


    @Override
    public DashboardResponse dashboardStatsByWorkspace(UUID workspaceId) {

        Long totalProjects = projectRepository.countByWorkspaceId(workspaceId);

//        Long totalTasks = tasksRepository.countByWorkspaceId(workspaceId);
//        Long todoTasks = tasksRepository.countByWorkspaceIdAndPriority(workspaceId, TaskPriority.TODO);
//        Long inProgressTasks = tasksRepository.countByWorkspaceIdAndPriority(workspaceId, TaskPriority.IN_PROGRESS);
//        Long doneTasks = tasksRepository.countByWorkspaceIdAndPriority(workspaceId, TaskPriority.DONE);
        TaskStatsProjection projection = tasksRepository.getTaskStats(workspaceId,
                TaskPriority.TODO,
                TaskPriority.IN_PROGRESS,
                TaskPriority.DONE
                );


        TaskStats taskStats = TaskStats.builder()
                .totalTasks(projection.getTotalTasks())
                .todoTasks(projection.getTodoTasks())
                .inProgressTasks(projection.getInProgressTasks())
                .doneTasks(projection.getDoneTasks())
                .build();




        Long totalProjectsInProgress = projectRepository.countByWorkspaceIdAndStatus(workspaceId, ProjectStatus.IN_PROGRESS);


        List<TaskPriorityDistributionResponse> priorityDistribution =
                tasksRepository.getTaskPriorityDistribution(workspaceId)
                        .stream()
                        .map(obj -> {

                            //Del Query del repo
                            Object[] row = (Object[]) obj;

                            return TaskPriorityDistributionResponse.builder()
                                    .priority((TaskStatus) row[0])
                                    .count(((Number) row[1]).longValue())
                                    .build();
                        })
                        .toList();


        List<TaskStatusDistributionResponse> statusDistribution =
                tasksRepository.getTaskStatusDistribution(workspaceId)
                        .stream()
                        .map(obj -> {

                            //Del Query del repo
                            Object[] row = (Object[]) obj;

                            return TaskStatusDistributionResponse.builder()
                                    .status((TaskPriority) row[0])
                                    .count(((Number) row[1]).longValue())
                                    .build();
                        })
                        .toList();


        return DashboardResponse.builder()
//                .inProgressTasks(inProgressTasks)
//                .todoTasks(todoTasks)
//                .doneTasks(doneTasks)
//                .totalTasks(totalTasks)
                .taskStats(taskStats)
                .totalProjects(totalProjects)
                .totalProjectInProgress(totalProjectsInProgress)
                .tasksStatusDistribution(statusDistribution)
                .tasksPriorityDistribution(priorityDistribution)
                .build();
    }


}
