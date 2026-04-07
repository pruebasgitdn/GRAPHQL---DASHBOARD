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
                ,taskAssigneeInput.getUserId(),user_id);


    }

        // Obtener todas las asignaciones

    // Obtener todas las asignaciones por user_id

    // Obtener todas las asignaciones por task_id

    // Eliminar asignacion

//    @QueryMapping(name = "getMembersAndRoles")
//    public List<WorkspaceMemberResponse> workspaceMembers(){
//
//        return  workspaceMemberService.findAll();
//    }

}
