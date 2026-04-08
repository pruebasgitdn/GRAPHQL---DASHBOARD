package com.back.controllers;
import com.back.entities.*;
import com.back.entities.dto.*;
import com.back.entities.mappers.TasksMapper;
import com.back.entities.mappers.WorkspaceMapper;
import com.back.security.UserDetailsImpl;
import com.back.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class GraphQLProjectController {

    private final ProjectService projectService;
    private final WorkspaceMapper workspaceMapper;
    private final TasksMapper tasksMapper;

    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "createProject")
    public ProjectResponse createProject(@Argument(name = "createProjectInput") CreateProjectInput createProjectInput,
                                         @AuthenticationPrincipal UserDetailsImpl authenticated

    ){
        if(authenticated == null){
            throw new AuthenticationCredentialsNotFoundException("No se econtraron las credenciales de autenticacion");
        }

        return projectService.createProject(createProjectInput);
    }

    @QueryMapping(name = "allProjects")
    public List<ProjectResponse> allProjects()
    {
        return projectService.allProjects();
    }


    @MutationMapping(name = "editProject")
    public ProjectResponse editProject(@Argument EditProjectInput editProjectInput,
                                       @Argument Long projectId
                                       ) {
        return projectService.editProject(projectId,editProjectInput);
    }

    @MutationMapping(name = "deleteProject")
    public Boolean deleteProject(@Argument Long projectId
    ) {
        return projectService.deleteProject(projectId);
    }



    @QueryMapping(name = "getProjectById")
    public ProjectResponse getProjectById(@Argument(name = "id") Long id ){
        return projectService.getProject(id);
    }

    @QueryMapping(name = "getProjectByWorkspace")
    public List<ProjectResponse> getProjectByWorkspace(@Argument(name = "workspaceId") UUID id ){
        return projectService.findAllByWorkspaceId(id);
    }




    //comentada la vuelta porque ya el dto devuelve sus respectivas relaciones
//    @SchemaMapping(typeName = "Project", field = "workspace")
//    public WorkspaceResponse workspace(Project project) {
//        return workspaceMapper.toResponseWithTasksCount(project.getWorkspace());
//    }
//    @SchemaMapping(typeName = "Project", field = "tasks")
//    public List<TaskResponse> tasks(Project project) {
//        return tasksMapper.mapTasks(project.getTasks());
//    }



}
