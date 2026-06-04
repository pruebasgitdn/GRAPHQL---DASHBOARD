package com.back.controllers;
import com.back.entities.dto.*;
import com.back.security.UserDetailsImpl;
import com.back.services.TaskAssigneeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class GrapQLTaskAssigneeController {

    private final TaskAssigneeService taskAssigneeService;

    //TODO: Crear muchas asignaciones => en base a los miembrosdelworkspace
    //TODO: Remover muchas asignaciones => ''....''

    // Crear asignacion
    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "createAssignation")
    public TaskAssigneeResponse createAssignation(@Valid @Argument CreateTaskAssigneeInput taskAssigneeInput,
                                                @AuthenticationPrincipal UserDetailsImpl authenticated) {

        if(authenticated == null){
            throw new AuthenticationCredentialsNotFoundException("No se econtraron las credenciales de autenticacion");
        }

        UUID user_id = authenticated.getId();

        return  taskAssigneeService.createAssignation(taskAssigneeInput.getTaskId()
                ,taskAssigneeInput.getUserId(),user_id,taskAssigneeInput.getWorkspaceId());


    }

    // Obtener todas las asignaciones
    @QueryMapping(name = "allAssignations")
    public List<TaskAssigneeResponse> allAssignations() {

        return  taskAssigneeService.allAssignations();

    }


    // Obtener todas las asignaciones por user_id
    @QueryMapping(name = "assignationsByUserId")
    public List<TaskAssigneeResponse> assignationsByUserId(@Valid @Argument UUID userId) {

        return  taskAssigneeService.assignationsByUserId(userId);

    }

    // Obtener todas las asignaciones por task_id
    @QueryMapping(name = "assignationsByTaskId")
    public List<TaskAssigneeResponse> assignationsByTaskId(@Valid @Argument Long taskId) {

        return  taskAssigneeService.assignationsByTaskId(taskId);

    }

    // Eliminar asignacion
    @PreAuthorize("isAuthenticated()")
    @QueryMapping(name = "deleteAssignationByTaskId")
    public Boolean deleteAssignationByTaskId(@Valid @Argument Long taskId,
            @Valid @Argument UUID workspaceId,
            @AuthenticationPrincipal UserDetailsImpl authenticated
    ){

        if(authenticated == null){
            throw new AuthenticationCredentialsNotFoundException("No se econtraron las credenciales de autenticacion");

        }

        return  taskAssigneeService.deleteAssignationByTaskId(taskId,authenticated.getId(),workspaceId);
    }



}
