package com.back.entities.mappers;
import com.back.entities.SubTask;
import com.back.entities.Task;
import com.back.entities.TaskAssignee;
import com.back.entities.TaskLabel;
import com.back.entities.dto.MyTasksDto;
import com.back.entities.dto.SubTaskResponse;
import com.back.entities.dto.TaskAssigneeResponse;
import com.back.entities.dto.TaskAssigneeResponseLblPrName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class TaskAssigneeMapper {
    private final UserMapper userMapper;
    private final TasksMapper taskMapper;



    public TaskAssigneeResponse toResponse(TaskAssignee entity) {
        return TaskAssigneeResponse.builder()
                .user(userMapper.toResponse(entity.getUser()))
                .task(taskMapper.toResponse(entity.getTask()))
                .build();
    }

    public TaskAssigneeResponseLblPrName toResponseLblPrName(TaskAssignee entity) {
        return TaskAssigneeResponseLblPrName.builder()
                .user(userMapper.toResponse(entity.getUser()))
                .task(taskMapper.toTaskLabelProjectDto(entity.getTask()))
                .build();
    }


    public MyTasksDto toMyTasksDto(
Task task
    ) {
        return MyTasksDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .isArchived(task.getIsArchived())
                .owner(userMapper.toResponse(task.getOwner()))
                .estimatedHours(task.getEstimatedHours())
                .actualHours(task.getActualHours())
                .completedAt(task.getCompletedAt())
                .createdAt(task.getCreatedAt())
                .dueDate(task.getDueDate())
                .project_id(task.getProject().getId())
                .workspace_id(task.getProject().getWorkspace().getId())
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

                .build();
    }




}
