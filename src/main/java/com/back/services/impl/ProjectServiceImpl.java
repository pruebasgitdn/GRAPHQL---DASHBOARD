package com.back.services.impl;

import com.back.entities.Project;
import com.back.entities.Task;
import com.back.entities.Workspace;
import com.back.entities.dto.CreateProjectInput;
import com.back.exceptions.AlreadyExistException;
import com.back.exceptions.GraphQLExceptionHandler;
import com.back.exceptions.ItemNotFoundException;
import com.back.repositories.ProjectRepository;
import com.back.repositories.TasksRepository;
import com.back.repositories.WorkspaceRepository;
import com.back.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final WorkspaceRepository workspaceRepository;
    private final TasksRepository tasksRepository;



    @Override
    public Project createProject(CreateProjectInput projectInput) {


        //Encontrar workspace
        Workspace workspace = workspaceRepository.findById(projectInput.getWorkspaceId()).orElseThrow(()->{
            throw new ItemNotFoundException("Worksapce no encontrado");
        });


        //Si hay tasks enviadas asginarlas
        List<Task> tasksListToAdd = new ArrayList<>();
        if(projectInput.getTaskIds() != null && !projectInput.getTaskIds().isEmpty())
        {
//           tasksListToAdd = projectInput.getTaskIds();
           tasksListToAdd = tasksRepository.findAllById(projectInput.getTaskIds());
        }

        List<Project> allP = projectRepository.findAll();

        boolean exist = allP.stream().anyMatch(p-> p.getName().equalsIgnoreCase(projectInput.getName()));
        //No mismo name si sabe
        if(exist){
            throw  new AlreadyExistException("Ya existe un projecto con ese nombre, intenta con otro.");
        }

        //Crear pryecto y retornar
        Project project = Project
                .builder()
                .name(projectInput.getName())
                .description(projectInput.getDescription())
                .workspace(workspace)
                .tasks(tasksListToAdd)
                .build();

        return projectRepository.save(project);
    }

    @Override
    public List<Project> allProjects() {

        List<Project> projects = projectRepository.findAll();

        if(projects.isEmpty()){
            throw new ItemNotFoundException("No se se encontraron proyectos");
        }

        return projects;
    }
}
