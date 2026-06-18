package com.back.services.impl;

import com.back.entities.*;
import com.back.entities.dto.NotificationResponse;
import com.back.entities.dto.TaskAssigneeResponse;
import com.back.entities.dto.TaskResponse;
import com.back.entities.dto.UserResponse;
import com.back.entities.mappers.NotificationMapper;
import com.back.entities.mappers.TaskAssigneeMapper;
import com.back.entities.mappers.TasksMapper;
import com.back.entities.mappers.UserMapper;
import com.back.enums.NotificationType;
import com.back.exceptions.ItemNotFoundException;
import com.back.repositories.*;
import com.back.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
public class TaskAssigneeServiceImpl implements TaskAssigneeService {

    private final TaskAssigneeRepository taskAssigneeRepository;
    private final NotificationRepository notificationRepository;
    private final TaskAssigneeMapper taskAssigneeMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TasksRepository tasksRepository;
    private final TasksService tasksService;
    private final WorkspaceMemberService workspaceMemberService;
    private final TasksMapper tasksMapper;
    private final UserMapper userMapper;
    private final NotificationPublisherService notificationPublisher;
    private final NotificationMapper notificationMapper;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    @Override
    @Transactional
    public Boolean deleteAssignationByTaskId(Long taskId, UUID currentUser,UUID workspaceId) {

        //encontrar tarea => aca mismo en el service tira el trhow de si no existe ...
        TaskResponse taskResponse = tasksService.getTask(taskId);

        //encontrar user
        UserResponse user = userService.findById(currentUser);


        //ecntonrar espacio de trabjo
        //UUID workspaceId = taskResponse.getProject().getWorkspace().getId();


        if (!workspaceMemberService.isAdminOrOwner(currentUser, workspaceId)) {
            throw new RuntimeException("No tienes permisos para asignar tareas en este espacio de trabajo");
        }


        taskAssigneeRepository.deleteByTaskId(taskResponse.getId());
        return true;
    }

    @Transactional
    @Override
    public TaskAssigneeResponse createAssignation(Long taskId, UUID user_assign, UUID currentUser,UUID workspaceId) {

        //encontrar tarea
        TaskResponse taskResponse = tasksService.getTask(taskId);
        if(taskResponse == null){
            throw new ItemNotFoundException("Tarea no encontrada");
        }

        //encontrar user
        UserResponse user = userService.findById(user_assign);
        if(user == null){
            throw new ItemNotFoundException("Usuario no encontrado");
        }

        //ecntonrar espacio de trabjo
       // UUID workspaceId = taskResponse.getProject().getWorkspace().getId();
        if(workspaceId == null){
            throw new ItemNotFoundException("Espacio de trabajo no encontrado");
        }

        if (!workspaceMemberService.isMember(workspaceId,currentUser)) {
            throw new RuntimeException("No eres miembro del espacio para asignar una tarea");
        }

        if(!workspaceMemberService.isMember(workspaceId,user.getId())){
            throw new RuntimeException("El usuario asignado no es miembro del espacio de trabajo");
        }


        User userEntity = userMapper.FromResponseToEntity(user);
        Task taskEntity =  tasksMapper.toEntity(taskResponse);

        TaskAssignee taskAssignee = TaskAssignee.builder()
                .task(taskEntity)
                .user(userEntity)
                .build();

        taskAssigneeRepository.save(taskAssignee);

        //Emitir notificacion
        NotificationResponse notification = NotificationResponse.builder()
                .user(user)
                .title("Tienes una nueva tarea asignada")
                .message("Se te ha asignado una nueva tarea")
                .type(NotificationType.ASSIGN)
                .build();

        notificationPublisher.publish(userEntity.getId().toString(),notification);
        Notification notificationEnt  = notificationMapper.toEntity(notification);
        notificationRepository.save(notificationEnt);


        TaskAssigneeResponse taskAssigneeResponse = TaskAssigneeResponse.builder()
                .task(taskResponse)
                .user(user)
                .build();

        return taskAssigneeResponse;
    }

    @Transactional
    @Override
    public List<TaskAssigneeResponse> createMultipleAssignations(Long taskId, List<UUID> userIds, UUID currentUser,UUID workspaceId) {
        //TaskResponse taskResponse = tasksService.getTask(taskId);
        Task taskEntity = tasksRepository.findById(taskId)
                .orElseThrow(() -> new ItemNotFoundException("Tarea no encontrada"));

        if(taskEntity == null){
            throw new ItemNotFoundException("Tarea no encontrada");
        }

        if(userIds.isEmpty()){
            throw new IllegalArgumentException("Ingrese el o los ids de los usuarios a asignar");
        }
        if (!workspaceMemberService.isMember(workspaceId,currentUser)) {
            throw new RuntimeException("No eres miembro del espacio para asignar una tarea");
        }


        Set<UUID> workspaceMembersSet = new HashSet<>(workspaceMemberRepository.findUserIdsByWorkspaceId(workspaceId));
        Set<UUID> uniqueUserIds = new HashSet<>(userIds);

        if(!workspaceMembersSet.containsAll(uniqueUserIds)){
            throw new RuntimeException("Uno o más usuarios no pertenecen al workspace");
        }

        //Task taskEntity = tasksMapper.toEntity(taskResponse);

        Set<UUID> alreadyAssigned =
                taskAssigneeRepository.findAssignedUserIdsByTaskId(taskId);
        uniqueUserIds.removeAll(alreadyAssigned);

        if(uniqueUserIds.isEmpty()){
            return List.of();
        }

        List<User> usersToAssign = userRepository.findAllById(uniqueUserIds);
        if(usersToAssign.size() != uniqueUserIds.size()){
            throw new ItemNotFoundException("Uno o más usuarios no existen");
        }

        List<TaskAssignee> assignees = usersToAssign.stream()
                .map(user -> TaskAssignee.builder()
                        .task(taskEntity)
                        .user(user)
                        .build())
                .toList();

        List<Notification>  notifications = usersToAssign.stream()
                        .map((n) -> Notification.builder()
                                .user(n)
                                .title("Tienes una nueva tarea asignada")
                                .message("Se te ha asignado una nueva tarea")
                                .type(NotificationType.ASSIGN)
                                .build())
                                .toList();

        notifications.forEach(nt ->
                notificationPublisher.publish(
                        nt.getUser().getId().toString(),
                        notificationMapper.toResponse(nt)
                )
        );

        List<TaskAssigneeResponse> assigneeResponses = assignees.stream()
                        .map((p)->{
                         return TaskAssigneeResponse.builder()
                                 .user(userMapper.toResponse(p.getUser()))
                                 .task(tasksMapper.toResponse(p.getTask()))
                                  .build();
                        }).toList();

        notificationRepository.saveAll(notifications);
        taskAssigneeRepository.saveAll(assignees);

        return assigneeResponses;
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
