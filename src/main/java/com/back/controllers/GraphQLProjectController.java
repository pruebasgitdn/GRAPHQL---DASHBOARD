package com.back.controllers;
import com.back.entities.*;
import com.back.entities.dto.CreateProjectInput;
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

    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "createProject")
    public Project createProject(@Argument(name = "createProjectInput") CreateProjectInput createProjectInput,
                                 @AuthenticationPrincipal UserDetailsImpl authenticated

    ){
        if(authenticated == null){
            throw new AuthenticationCredentialsNotFoundException("No se econtraron las credenciales de autenticacion");
        }

        return projectService.createProject(createProjectInput);
    }

    @QueryMapping(name = "allProjects")
    public List<Project> allProjects()
    {

        return projectService.allProjects();
    }


    @SchemaMapping(typeName = "Project", field = "workspace")
    public Workspace workspace(Project project) {
        return project.getWorkspace();
    }

    @SchemaMapping(typeName = "Project", field = "tasks")
    public List<Task> task(Project project) {
        return project.getTasks();
    }




}
