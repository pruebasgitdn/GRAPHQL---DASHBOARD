package com.back.controllers;
import com.back.dataloader.SubTaskDataLoader;
import com.back.dataloader.TaskDataLoader;
import com.back.entities.*;
import com.back.entities.dto.*;
import com.back.entities.mappers.TasksMapper;
import com.back.entities.mappers.WorkspaceMapper;
import com.back.security.UserDetailsImpl;
import com.back.services.ProjectService;
import com.back.services.WorkspaceMemberService;
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
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Controller
@RequiredArgsConstructor
public class GraphQLProjectController {

    private final ProjectService projectService;

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

    @SchemaMapping(typeName = "ProjectResponse", field = "tasks")
    public CompletableFuture<List<TaskResponse>> resolveTasks(
            ProjectResponse projectResponse,
            DataFetchingEnvironment environment
    ) {
        DataLoader<Long, List<TaskResponse>> loader =
                environment.getDataLoader(TaskDataLoader.TASK_LOADER);

        return loader.load(projectResponse.getId());
    }



}
