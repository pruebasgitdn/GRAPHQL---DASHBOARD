package com.back.controllers;
import com.back.entities.User;
import com.back.entities.dto.CreateWorkspaceInput;
import com.back.entities.dto.UserInput;
import com.back.entities.dto.WorkspaceResponse;
import com.back.repositories.UserRepository;
import com.back.security.UserDetailsImpl;
import com.back.services.WorkspaceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class GraphQLWorkspaceController {

    private final WorkspaceService workspaceService;
    private final UserRepository userRepository;

    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "createWorkspace")
    public WorkspaceResponse createWorkspace(@Valid @Argument CreateWorkspaceInput workspaceInput,
                                             @AuthenticationPrincipal UserDetailsImpl user){

        UUID user_id = user.getId();

        System.out.println(user_id);

        return workspaceService.createWorkspace(workspaceInput,user_id);
    }

    @QueryMapping(name = "workspaceList")
    public List<WorkspaceResponse> workspaceList(){
        return workspaceService.findAll();
    }

    @QueryMapping(name = "workspace")
    public WorkspaceResponse workspace(@Argument(name = "id") UUID id ){
        return workspaceService.findById(id);
    }


    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "addMembersToWorkspace")
    public WorkspaceResponse addMemberToWorkspace(@Argument(name = "userIds") List<UUID> users,
                                                  @Argument(name = "workspace_id") UUID workspace_id,
                                                  @AuthenticationPrincipal UserDetailsImpl authenticated

    ){
        if(authenticated == null){
            throw new AuthenticationCredentialsNotFoundException("No se econtraron las credenciales de autenticacion");
        }

        return workspaceService.addMemberToWorkspace(users,workspace_id,authenticated.getId());
    }


    @SchemaMapping(typeName = "WorkspaceResponse", field = "owner")
    public User resolveOwner(WorkspaceResponse workspace) {
        return userRepository.findById(workspace.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner no encontrado"));
    }




}
