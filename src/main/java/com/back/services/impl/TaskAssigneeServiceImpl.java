package com.back.services.impl;

import com.back.entities.*;
import com.back.entities.dto.TaskAssigneeResponse;
import com.back.entities.dto.TaskResponse;
import com.back.entities.dto.UserResponse;
import com.back.entities.mappers.TasksMapper;
import com.back.entities.mappers.UserMapper;
import com.back.repositories.TaskAssigneeRepository;
import com.back.repositories.TasksRepository;
import com.back.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;



@Service
@RequiredArgsConstructor
public class TaskAssigneeServiceImpl implements TaskAssigneeService {

    private final TaskAssigneeRepository taskAssigneeRepository;
    private final TasksService tasksService;
    private final WorkspaceMemberService workspaceMemberService;
    private final UserService userService;
    private final TasksMapper tasksMapper;
    private final UserMapper userMapper;


    @Override
    public Boolean deleteAssignation(Long taskId) {
        return null;
    }

    @Override
    public TaskAssigneeResponse createAssignation(Long taskId, UUID user_assign, UUID currentUser) {

        //encontrar tarea
        TaskResponse taskResponse = tasksService.getTask(taskId);

        //encontrar user
        UserResponse user = userService.findById(user_assign);

        //ecntonrar espacio de trabjo
        UUID workspaceId = taskResponse.getProject().getWorkspace().getId();

        if (!workspaceMemberService.isAdminOrOwner(currentUser, workspaceId)) {
            throw new RuntimeException("No tienes permisos para asignar tareas en este espacio de trabajo");
        }

        TaskAssignee taskAssignee = TaskAssignee.builder()
                .task(tasksMapper.toEntity(taskResponse))
                .user(userMapper.FromResponseToEntity(user))
                .build();

        taskAssigneeRepository.save(taskAssignee);

        TaskAssigneeResponse taskAssigneeResponse = TaskAssigneeResponse.builder()
                .task(taskResponse)
                .user(user)
                .build();

        return taskAssigneeResponse;
    }
}
