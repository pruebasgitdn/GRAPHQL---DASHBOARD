package com.back.services.impl;

import com.back.entities.Project;
import com.back.entities.Workspace;
import com.back.entities.dto.CreateProjectInput;
import com.back.exceptions.GraphQLExceptionHandler;
import com.back.exceptions.ItemNotFoundException;
import com.back.repositories.ProjectRepository;
import com.back.repositories.WorkspaceRepository;
import com.back.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final WorkspaceRepository workspaceRepository;



    @Override
    public Project createProject(CreateProjectInput projectInput, UUID workspace_id) {

        //Encontrar workspace
        Workspace workspace = workspaceRepository.findById(workspace_id).orElseThrow(()->{
            throw new ItemNotFoundException("Worksapce no encontrado por el id :{}"+workspace_id);
        });

        //Crear pryecto y retornar
        Project project = Project
                .builder()
                .name(projectInput.getName())
                .description(projectInput.getDescription())
                .workspace(workspace)
                .build();

        return projectRepository.save(project);
    }
}
