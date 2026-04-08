package com.back.services.impl;

import com.back.entities.*;
import com.back.entities.dto.TaskAssigneeResponse;
import com.back.entities.dto.TaskResponse;
import com.back.entities.dto.UserResponse;
import com.back.entities.mappers.TaskAssigneeMapper;
import com.back.entities.mappers.TasksMapper;
import com.back.entities.mappers.UserMapper;
import com.back.exceptions.ItemNotFoundException;
import com.back.repositories.TaskAssigneeRepository;
import com.back.repositories.TasksRepository;
import com.back.repositories.UserRepository;
import com.back.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;



@Service
@RequiredArgsConstructor
public class TaskAssigneeServiceImpl implements TaskAssigneeService {

    private final TaskAssigneeRepository taskAssigneeRepository;
    private final TaskAssigneeMapper taskAssigneeMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TasksRepository tasksRepository;
    private final TasksService tasksService;
    private final WorkspaceMemberService workspaceMemberService;
    private final TasksMapper tasksMapper;
    private final UserMapper userMapper;


    @Override
    @Transactional
    public Boolean deleteAssignationByTaskId(Long taskId, UUID currentUser) {

        //encontrar tarea => aca mismo en el service tira el trhow de si no existe ...
        TaskResponse taskResponse = tasksService.getTask(taskId);

        //encontrar user
        UserResponse user = userService.findById(currentUser);


        //ecntonrar espacio de trabjo
        UUID workspaceId = taskResponse.getProject().getWorkspace().getId();


        if (!workspaceMemberService.isAdminOrOwner(currentUser, workspaceId)) {
            throw new RuntimeException("No tienes permisos para asignar tareas en este espacio de trabajo");
        }


        taskAssigneeRepository.deleteByTaskId(taskResponse.getId());
        return true;
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


        if(!workspaceMemberService.isMember(workspaceId,user.getId())){
            throw new RuntimeException("El usuario asignado no es miembro del espacio de trabajo");
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

    @Transactional(readOnly = true)
    @Override
    public List<TaskAssigneeResponse> assignationsByUserId(UUID ownerId) {

        //verificar existencai
        Optional<User> user = userRepository.findById(ownerId);

        if(user.isEmpty()){
            throw new ItemNotFoundException("Usuario no encontrado");
        }

        List<TaskAssignee> taskAssignees = taskAssigneeRepository.findAllByUserId(user.get().getId());

        if(taskAssignees.isEmpty()){
          throw new ItemNotFoundException("No se se encontraron tareas asignadas a este id del usuario: "+user.get().getId());
        }


        return taskAssignees.stream()
                .map(taskAssigneeMapper::toResponse)
                .toList();
    }

    @Override
    public List<TaskAssigneeResponse> assignationsByTaskId(Long taskId) {
        //verificar existencai
        //Optional<User> user = userRepository.findById(ownerId);

        Optional<Task> task =  tasksRepository.findById(taskId);


        if(task.isEmpty()){
            throw new ItemNotFoundException("Tarea no encontrada");
        }

        List<TaskAssignee> taskAssignees = taskAssigneeRepository.findAllByTaskId(task.get().getId());

        if(taskAssignees.isEmpty()){
            throw new ItemNotFoundException("No se se encontraron tareas asignadas a este id : "+task.get().getId());
        }


        return taskAssignees.stream()
                .map(taskAssigneeMapper::toResponse)
                .toList();
    }

    @Override
    public List<TaskAssigneeResponse> allAssignations() {
        List<TaskAssignee> allAssignees =  taskAssigneeRepository.findAll();

        return allAssignees.stream().map(taskAssigneeMapper::toResponse).toList();
    }


}
