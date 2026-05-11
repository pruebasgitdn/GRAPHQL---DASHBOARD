package com.back.services;

import com.back.entities.Task;
import com.back.entities.dto.TaskResponse;
import com.back.entities.dto.UserResponse;
import com.back.entities.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TaskMappingService {

    private final UserMapper userMapper;

    //sinproject para evitar dependencias circulares
    public TaskResponse toResponseWithoutProject(Task task){

        UserResponse owner = userMapper.toResponse(task.getOwner());

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .isArchived(task.getIsArchived())
                .estimatedHours(task.getEstimatedHours())
                .actualHours(task.getActualHours())
                .completedAt(task.getCompletedAt())
                .priority(task.getPriority())
                .owner(owner)
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .build();
    }

}
