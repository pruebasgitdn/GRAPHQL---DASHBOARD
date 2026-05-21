package com.back.entities.mappers;


import com.back.entities.Project;
import com.back.entities.SubTask;
import com.back.entities.dto.*;
import com.back.repositories.TasksRepository;
import com.back.services.TaskMappingService;
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
//    private final TasksMapper tasksMapper;
    private final TasksRepository tasksRepository;

    private final TaskMappingService taskMappingService;

    public ProjectResponse toResponseWithTasksCount(Project project, Long taskCount) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .owner(userMapper.toResponse(project.getOwner()))
                .status(project.getStatus())
                .dueDate(project.getDueDate())
                .startDate(project.getStartDate())
                .tasks(
                project.getTasks() == null
                        ? List.of()
                        : project.getTasks().stream()
                        //sinproject para evitar dependencias circulares
                        .map(p ->taskMappingService.toResponseWithoutProject(p)).toList())
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
                .tasks(
                        project.getTasks() == null
                                ? List.of()
                                : project.getTasks().stream()
                                //sinproject para evitar dependencias circulares
                                .map(p ->taskMappingService.toResponseWithoutProject(p)).toList())
                .workspace(workspaceMapper.toResponseWithoutCount(project.getWorkspace()))
                .build();
    }

    public ProjectSimpleResponse toSimpleResponseWithoutCount(Project project){

        return ProjectSimpleResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .tasksCount((long) project.getTasks().size())
                .createdAt(project.getCreatedAt())
                .startDate(project.getStartDate())
                .status(project.getStatus())
                .build();
    }





    public void updateProjectFromDto(EditProjectInput input, Project project) {

        if (input.getName() != null) {
            project.setName(input.getName());
        }

        if (input.getDescription() != null) {
            project.setDescription(input.getDescription());        }



    }


}
