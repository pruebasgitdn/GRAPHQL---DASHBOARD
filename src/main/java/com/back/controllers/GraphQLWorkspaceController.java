package com.back.controllers;
import com.back.dataloader.ProjectDataLoader;
import com.back.dataloader.TaskDataLoader;
import com.back.entities.User;
import com.back.entities.dto.*;
import com.back.repositories.UserRepository;
import com.back.security.UserDetailsImpl;
import com.back.services.WorkspaceMemberService;
import com.back.services.WorkspaceService;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
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
import java.util.concurrent.CompletableFuture;


@Controller
@RequiredArgsConstructor
public class GraphQLWorkspaceController {

    private final WorkspaceService workspaceService;
    private final UserRepository userRepository;

    //TODO: Implementar dataloader para owner o schemamapping
    //TODO: Dataloader para owner

    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "createWorkspace")
    public WorkspaceResponse createWorkspace(@Valid @Argument CreateWorkspaceInput workspaceInput,
                                             @AuthenticationPrincipal UserDetailsImpl user){

        UUID user_id = user.getId();


        return workspaceService.createWorkspace(workspaceInput,user_id);
    }

    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "editWorkspace")
    public WorkspaceResponse editWorkspace(@Valid @Argument EditWorkspaceInput workspaceInput,
                                           @Valid @Argument UUID workspaceId,
                                             @AuthenticationPrincipal UserDetailsImpl user){

        UUID user_id = user.getId();

        return workspaceService.editWorkspace(workspaceInput,workspaceId,user_id);
    }

    @QueryMapping(name = "workspaceList")
    public List<WorkspaceResponse> workspaceList(){
        return workspaceService.findAll();
    }

    @QueryMapping(name = "workspace")
    public WorkSpaceDetailResponse workspace(@Argument(name = "id") UUID id ){
        return workspaceService.findById(id);
    }


    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "addMembersToWorkspace")
    public WorkspaceResponse addMemberToWorkspace(@Argument(name = "userIds") List<UUID> users,
                                                  @Argument(name = "workspaceId") UUID workspace_id,
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

    //removeMembersFromWorkspace
    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "removeMembersFromWorkspace")
    public WorkspaceResponse removeMembersFromWorkspace(@Argument(name = "userIds") List<UUID> users,
                                                  @Argument(name = "workspaceId") UUID workspace_id,
                                                  @AuthenticationPrincipal UserDetailsImpl authenticated
    ){
        if(authenticated == null){
            throw new AuthenticationCredentialsNotFoundException("No se econtraron las credenciales de autenticacion");
        }

        return workspaceService.removeMembersFromWorkspace(users,workspace_id,authenticated.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "removeWorkspace")
    public Boolean removeWorkspace(@Argument(name = "workspaceId") UUID workspace_id,
                                             @AuthenticationPrincipal UserDetailsImpl authenticated
    ){
        if(authenticated == null){
            throw new AuthenticationCredentialsNotFoundException("No se econtraron las credenciales de autenticacion");
        }

        return workspaceService.removeWorkspace(workspace_id,authenticated.getId());
    }


@SchemaMapping(typeName = "WorkSpaceDetailResponse", field = "projects")
public CompletableFuture<List<ProjectSimpleResponse>> resolveProjects(
        WorkSpaceDetailResponse workSpaceDetailResponse,
        DataFetchingEnvironment environment
) {

    DataLoader<UUID, List<ProjectSimpleResponse>> loader =
            environment.getDataLoader(ProjectDataLoader.PROJECT_LOADER);

    return loader.load(workSpaceDetailResponse.getId());
}


    //El de editar name porque sus relaciones se manejan desde los servicios
    //De sus respectivas entidades



}
