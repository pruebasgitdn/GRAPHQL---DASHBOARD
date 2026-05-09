package com.back.controllers;


import com.back.entities.Task;
import com.back.entities.User;
import com.back.entities.dto.*;
import com.back.entities.mappers.TasksMapper;
import com.back.security.UserDetailsImpl;
import com.back.services.SubTaskService;
import com.back.services.TasksService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GraphQLTasksController {

    private final TasksService tasksService;
    private final SubTaskService subTaskService;


    //  Crear task
    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "createTask")
    public TaskResponse createTask(@Argument(name = "taskInput") CreateTaskInput createTaskInput,
                                   @AuthenticationPrincipal UserDetailsImpl authenticated

    ){
        if(authenticated == null){
            throw new AuthenticationCredentialsNotFoundException("No se econtraron las credenciales de autenticacion");
        }

        return tasksService.createTask(createTaskInput,authenticated.getId());
    }

    // Tareas

    @QueryMapping(name = "allTasks")
    public List<TaskResponse> allTasks(
    ){

       return tasksService.findAllTasks();
    }

    // Tarea por id
    @QueryMapping(name = "getTask")
    public TaskResponse getTask(@Argument(name = "id") Long id
    ){

        return tasksService.getTask(id);
    }

    // Tareas por id del proyecto
    @QueryMapping(name = "getTasksByProjectId")
    public List<TaskResponse> getTasksByProjectId(@Argument(name = "projectId") Long id
    ){
       return tasksService.findAllByProjectId(id);
    }


    // Eliminar task => isadminorowner
    @MutationMapping(name = "deleteTask")
    public Boolean deleteTask(@Argument(name = "taskId") Long taskId
    ){

        return tasksService.deleteTask(taskId);
    }


    // Editar task => isadminorwoner
    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "editTask")
    public TaskResponse editTask(@Argument(name = "taskId") Long taskId,
                                   @Argument(name = "editTaskInput") EditTaskInput editTaskInput,
                                   @AuthenticationPrincipal UserDetailsImpl authenticated

    ){
        if(authenticated == null){
            throw new AuthenticationCredentialsNotFoundException("No se econtraron las credenciales de autenticacion");
        }

        return tasksService.editTask(taskId,editTaskInput);
    }

    @SchemaMapping(typeName = "TaskResponse", field = "subTasks")
    public List<SubTaskResponse> resolveSubTasks(TaskResponse taskResponse) {

        return subTaskService.getSubTasksByTaskId(taskResponse.getId());

    }



}
