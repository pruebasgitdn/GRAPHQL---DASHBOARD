package com.back.entities.mappers;


import com.back.entities.Task;
import com.back.entities.dto.EditTaskInput;
import com.back.entities.dto.ProjectResponse;
import com.back.entities.dto.TaskResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TasksMapper {

    private final ProjectMapper projectMapper;

    public List<TaskResponse> mapTasks(List<Task> tasks) {
        return tasks.stream()
                .map(task -> TaskResponse.builder()
                        .id(task.getId())
                        .project(projectMapper.toResponseWithoutCount(task.getProject()))
                        .description(task.getDescription())
                        .title(task.getTitle())
                        .priority(task.getPriority())
                        .status(task.getStatus())
                        .build())
                .toList();
    }

    public TaskResponse toResponse(Task task){
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .project(projectMapper.toResponseWithoutCount(task.getProject()))
                .status(task.getStatus())
                .priority(task.getPriority())
                .build();
    }

    public Task toEntity(TaskResponse task){
        return Task.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .project(projectMapper.toEntity(task.getProject()))
                .status(task.getStatus())
                .priority(task.getPriority())
                .build();
    }

    public void updateTaskFromDto(EditTaskInput input, Task task) {

        if (input.getTitle() != null) {
            task.setTitle(input.getTitle());
        }

        if (input.getDescription() != null) {
            task.setDescription(input.getDescription());
        }

        if (input.getStatus() != null) {
            task.setStatus(input.getStatus());
        }

        if (input.getPriority() != null) {
            task.setPriority(input.getPriority());
        }
    }
}
