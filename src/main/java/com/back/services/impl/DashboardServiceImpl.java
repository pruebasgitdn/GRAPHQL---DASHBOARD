package com.back.services.impl;

import com.back.entities.Task;
import com.back.entities.dto.*;
import com.back.entities.mappers.ProjectMapper;
import com.back.entities.mappers.TasksMapper;
import com.back.entities.mappers.WorkspaceMapper;
import com.back.enums.ProjectStatus;
import com.back.enums.TaskPriority;
import com.back.repositories.ProjectRepository;
import com.back.repositories.TasksRepository;
import com.back.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final TasksRepository tasksRepository;
    private final ProjectRepository projectRepository;
    private final WorkspaceMapper workspaceMapper;
    private final TasksMapper taskMapper;
    private final ProjectMapper projectMapper;



    @Override

    //TODO: MEter el cacheable ajustarlo a todos los metodos de task
    //TODO: todos los meotods de projects y workpsace
    @Cacheable(value = "dashboard", key = "#workspaceId")
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

        Long totalProjectsInProgress = projectRepository.countByWorkspaceIdAndStatus(workspaceId, ProjectStatus.IN_PROGRESS);


        TaskStats taskStats = TaskStats.builder()
                .totalTasks(projection.getTotalTasks())
                .totalTodoTasks(projection.getTodoTasks())
                .totalInProgressTasks(projection.getInProgressTasks())
                .totalDoneTasks(projection.getDoneTasks())
                .totalProjects(totalProjects)
                .totalProjectInProgress(totalProjectsInProgress)
                .build();



        List<TaskStatusDistributionResponse> priorityDistribution =
                tasksRepository.getTaskPriorityDistribution(workspaceId)
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


        List<ProjectStatusDistributionResponse> projectStatusDistribution =
                projectRepository.getProjectStatusDistribution(workspaceId)
                        .stream()
                        .map(obj -> {

                            //Del Query del repo
                            Object[] row = (Object[]) obj;

                            return ProjectStatusDistributionResponse.builder()
                                    .status((ProjectStatus) row[0])
                                    .count(((Number) row[1]).longValue())
                                    .build();
                        })
                        .toList();


        List<WorkspaceProductivityData> productivityData =
                projectRepository.getWorkspaceProductivity(workspaceId)
                        .stream()
                        .map(row -> WorkspaceProductivityData.builder()
                                .name((String) row[0])
                                .total(((Number)row[1]).longValue())
                                .completed(((Number)row[2]).longValue())
                                .build()
                        )
                        .toList();


        LocalDate todayUT = LocalDate.now();
        LocalDate nextWeek = todayUT.plusDays(7);

        List<TaskResponse> upcomingTasks =
                tasksRepository.findUpcomingTasks(workspaceId, todayUT, nextWeek)
                        .stream()
                        .map(taskMapper::toResponseWithoutProject).toList();

        List<ProjectResponse> recentProjectList =
                projectRepository.findTop5ByWorkspaceIdOrderByCreatedAtDesc(workspaceId)
                        .stream()
                        .map(projectMapper::toResponseWithoutCount).toList();



        //Para los anteriores 30 dias tasktrends
        LocalDate today = LocalDate.now();

        LocalDateTime startDate = today.minusMonths(1).atStartOfDay();
        LocalDateTime endDate = today.atStartOfDay();


        List<TaskTrendCL> taskTrends = tasksRepository.perramalparida(workspaceId,startDate,endDate)
                .stream()
                .map(t -> new TaskTrendCL(
                        t.getDay().atStartOfDay(),
                        t.getPriority(),
                        t.getTotal()))
                .toList();




        return DashboardResponse.builder()
                .taskStats(taskStats)
                .projectStatusDistribution(projectStatusDistribution)
                .tasksPriorityDistribution(priorityDistribution)
                .workspaceProductivityData(productivityData)
                .taskTrendsData(taskTrends)
                .upcomingTasks(upcomingTasks)
                .recentProjects(recentProjectList)
                .build();
    }

    @Override
    public String dashboardTest(UUID workspaceId) {

        LocalDate today = LocalDate.now();

        LocalDateTime startDate = today.minusDays(30).atStartOfDay();
        LocalDateTime endDate = today.atTime(LocalTime.MAX);

        List<Task> tasks = tasksRepository.test(workspaceId, startDate, endDate);
         System.out.print("Tasks encontradas: " + tasks.size());
        return "Tasks encontradas: " + tasks.size();
    }

    @Override
    public List<Task> dashboardTestDos(UUID workspaceId) {
        return tasksRepository.testdos(workspaceId);
    }

    @Override
    public  List<Task> dashboardTestTres(UUID workspaceId) {

        LocalDate today = LocalDate.now();

        LocalDateTime startDate = today.minusDays(30).atStartOfDay();
        LocalDateTime endDate = today.atTime(LocalTime.MAX);
        return tasksRepository.testtres(startDate,endDate);
    }

    @Override
    public List<TaskTrendCL> perrota(UUID workspaceId) {

        LocalDate today = LocalDate.now();

        LocalDateTime startDate = today.minusMonths(1).atStartOfDay();
        LocalDateTime endDate = today.atStartOfDay();


        List<TaskTrendCL> trends = tasksRepository.perramalparida(workspaceId,startDate,endDate)
    .stream()
                .map(t -> new TaskTrendCL(
                        t.getDay().atStartOfDay(),
                        t.getPriority(),
                        t.getTotal()))
                .toList();

        return  trends;

    }

    @Override
    public List<ProjectResponse> donomar(UUID workspaceId) {

        List<ProjectResponse> projectResponseList =
                projectRepository.findTop5ByWorkspaceIdOrderByCreatedAtDesc(workspaceId)
                        .stream()
                        .map(projectMapper::toResponseWithoutCount).toList();

    return  projectResponseList;

    }

    @Override
    public String current_database() {
       return tasksRepository.currentDatabase();
    }

    @Override
    public String inet_server_addr() {
       return tasksRepository.inet_server_addr();
    }

    @Override
    public Long nancy() {
        return tasksRepository.nancy();
    }


}
