package com.back.controllers;


import com.back.dataloader.SubTaskDataLoader;
import com.back.entities.dto.*;
import com.back.enums.TaskPriority;
import com.back.enums.TaskStatus;
import com.back.security.UserDetailsImpl;
import com.back.services.SubTaskService;
import com.back.services.TasksService;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    @MutationMapping(name = "editTaskTitle")
    public Boolean editTaskTitle(
            @Argument(name = "id") Long id,
            @Argument(name = "newTitle") String newTitle
    ){

        return tasksService.editTaskTitle(newTitle,id);
    }

    @MutationMapping(name = "editTaskDescription")
    public Boolean editTaskDescription(
            @Argument(name = "id") Long id,
            @Argument(name = "newDescription") String newDescription
    ){
        return tasksService.editTaskDescription(newDescription,id);
    }

    @MutationMapping(name = "editTaskStatus")
    public Boolean editTaskStatus(
            @Argument(name = "id") Long id,
            @Argument(name = "newStatus") TaskStatus newStatus
    ){
        return tasksService.editTaskStatus(newStatus,id);
    }

    @MutationMapping(name = "editTaskPriority")
    public Boolean editTaskPriority(
            @Argument(name = "id") Long id,
            @Argument(name = "newPriority") TaskPriority newPriority
    ){
        return tasksService.editTaskPriority(newPriority,id);
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

//    @SchemaMapping(typeName = "TaskResponse", field = "subTasks")
//    public List<SubTaskResponse> resolveSubTasks(TaskResponse taskResponse) {
//
//        return subTaskService.getSubTasksByTaskId(taskResponse.getId());
//
//    }

    @SchemaMapping(typeName = "TaskResponse", field = "subTasks")
    public CompletableFuture<List<SubTaskResponse>> resolveSubTasks(
            TaskResponse taskResponse,
            DataFetchingEnvironment environment
    ) {
        DataLoader<Long, List<SubTaskResponse>> loader =
                environment.getDataLoader(SubTaskDataLoader.SUBTASK_LOADER);

        return loader.load(taskResponse.getId());
    }



}
