package com.back.entities.mappers;


import com.back.entities.Project;
import com.back.entities.dto.EditProjectInput;
import com.back.entities.dto.ProjectResponse;
import com.back.repositories.TasksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProjectMapper {

    private final WorkspaceMapper workspaceMapper;
    private final UserMapper userMapper;
    private final TasksRepository tasksRepository;

    public ProjectResponse toResponseWithTasksCount(Project project, Long taskCount) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .owner(userMapper.toResponse(project.getOwner()))
                .status(project.getStatus())
                .dueDate(project.getDueDate())
                .startDate(project.getStartDate())
                .workspace(workspaceMapper.toResponseWithoutCount(project.getWorkspace()))
                .tasksCount(taskCount)
                .build();
    }

    public Project toEntity(ProjectResponse project) {
        return Project.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .owner(userMapper.FromResponseToEntity(project.getOwner()))
                .status(project.getStatus())
                .dueDate(project.getDueDate())
                .startDate(project.getStartDate())
                .workspace(workspaceMapper.toEntityWithoutCount(project.getWorkspace()))
                .build();
    }

    public ProjectResponse toResponseWithoutCount(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .owner(userMapper.toResponse(project.getOwner()))
                .status(project.getStatus())
                .dueDate(project.getDueDate())
                .startDate(project.getStartDate())
                .workspace(workspaceMapper.toResponseWithoutCount(project.getWorkspace()))
                .build();
    }



    public List<ProjectResponse> mapProjects(List<Project> projects) {
        return projects.stream()
                .map(project -> ProjectResponse.builder()
                        .id(project.getId())
                        //.tasks(tasksMapper.mapTasks(project.getTasks()))
                        .workspace(workspaceMapper.toResponseWithoutCount(project.getWorkspace()))
                        .name(project.getDescription())
                        .name(project.getName())
                        .build())
                .toList();
    }


    public void updateProjectFromDto(EditProjectInput input, Project project) {

        if (input.getName() != null) {
            project.setName(input.getName());
        }

        if (input.getDescription() != null) {
            project.setDescription(input.getDescription());        }



    }


}
