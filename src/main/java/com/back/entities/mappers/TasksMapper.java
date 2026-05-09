package com.back.entities.mappers;


import com.back.entities.Task;
import com.back.entities.TaskLabel;
import com.back.entities.SubTask;
import com.back.entities.User;
import com.back.entities.dto.*;
import com.back.enums.TaskLabelType;
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
    private final UserMapper  userMapper;

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

        UserResponse owner = userMapper.toResponse(task.getOwner());

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .project(projectMapper.toResponseWithoutCount(task.getProject()))
                .status(task.getStatus())
                .isArchived(task.getIsArchived())
                .estimatedHours(task.getEstimatedHours())
                .actualHours(task.getActualHours())
                .completedAt(task.getCompletedAt())
                .priority(task.getPriority())
                .owner(owner)
                .dueDate(task.getDueDate())
                .labels(
                        task.getLabels() == null
                                ? List.of()
                                : task.getLabels().stream()
                                .map(TaskLabel::getLabel)
                                .toList()
                )
                .subTasks(
                        task.getSubtasks() == null
                                ? List.of()
                                : task.getSubtasks().stream()
                                .map((SubTask subTask) -> SubTaskResponse.builder()
                                        .id(subTask.getId())
                                        .title(subTask.getTitle())
                                        .completed(subTask.getCompleted())
                                        .createdAt(subTask.getCreatedAt())
                                        .build()
                                )
                                .toList()
                )
                .createdAt(task.getCreatedAt())
                .build();
    }

    public Task toEntity(TaskResponse task){



        return Task.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .project(projectMapper.toEntity(task.getProject()))
                .status(task.getStatus())
                .isArchived(task.getIsArchived())
                .estimatedHours(task.getEstimatedHours())
                .actualHours(task.getActualHours())
                .completedAt(task.getCompletedAt())
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
