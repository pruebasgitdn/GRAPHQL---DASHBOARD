package com.back.services.impl;

import com.back.entities.Project;
import com.back.entities.Task;
import com.back.entities.Workspace;
import com.back.entities.dto.CreateProjectInput;
import com.back.entities.dto.EditProjectInput;
import com.back.entities.dto.ProjectResponse;
import com.back.entities.dto.WorkSpaceDetailResponse;
import com.back.entities.mappers.ProjectMapper;
import com.back.entities.mappers.TasksMapper;
import com.back.entities.mappers.UserMapper;
import com.back.entities.mappers.WorkspaceMapper;
import com.back.exceptions.AlreadyExistException;
import com.back.exceptions.ItemNotFoundException;
import com.back.repositories.ProjectRepository;
import com.back.repositories.TasksRepository;
import com.back.repositories.WorkspaceRepository;
import com.back.services.ProjectService;
import com.back.services.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceService workspaceService;
    private final TasksRepository tasksRepository;

    private final TasksMapper tasksMapper;
    private final WorkspaceMapper workspaceMapper;
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;



    @Override
    public ProjectResponse createProject(CreateProjectInput projectInput) {

        //Encontrar workspace
        Workspace workspace = workspaceRepository.findById(projectInput.getWorkspaceId()).orElseThrow(()->{
            throw new ItemNotFoundException("Worksapce no encontrado");
        });


        String name = projectInput.getName().trim(); //Normalizar
        //Validar
        if(projectRepository.existsByNameAndWorkspaceId(name,workspace.getId())){
            throw new AlreadyExistException("Ya existe un proyecto con ese nombre en este workspace");
        }

        //Si hay tasks enviadas asginarlas
        List<Task> tasksListToAdd = new ArrayList<>();
        if(projectInput.getTaskIds() != null && !projectInput.getTaskIds().isEmpty())
        {
           tasksListToAdd = tasksRepository.findAllById(projectInput.getTaskIds());
        }


        //Crear pryecto,guardar y retornar
        Project project = Project
                .builder()
                .name(projectInput.getName())
                .description(projectInput.getDescription())
                .workspace(workspace)
                .tasks(tasksListToAdd)
                .build();

         projectRepository.save(project);
         Long countTasks = tasksRepository.countByProjectId(project.getId());

        return  projectMapper.toResponseWithTasksCount(project,countTasks);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectResponse> allProjects() {

        List<Project> projects = projectRepository.findAll();

        if(projects.isEmpty()){
            throw new ItemNotFoundException("No se se encontraron proyectos");
        }

        return projects.stream()
                .map(pro -> {
                    Long count = tasksRepository.countByProjectId(pro.getId());
                    return projectMapper.toResponseWithTasksCount(pro,count);
                }).toList();

    }

    @Transactional(readOnly = true)
    @Override
    public ProjectResponse getProject(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(()->{
            throw  new ItemNotFoundException("No se se encontraron proyectos por el id: "+id);
        });

        Long count = tasksRepository.countByProjectId(project.getId());

        return projectMapper.toResponseWithTasksCount(project,count);
    }

    @Override
    public List<ProjectResponse> findAllByWorkspaceId(UUID id) {

        List<Project> projectList = projectRepository.findAllByWorkspaceId(id);

        if(projectList.isEmpty()){
            throw new ItemNotFoundException("No se se encontraron proyectos por el workspaceid: "+ id);
        }


        return projectList.stream()
                .map(pro -> {
                    Long count = tasksRepository.countByProjectId(pro.getId());
                    return projectMapper.toResponseWithTasksCount(pro,count);
                }).toList();

    }

    @Override
    public ProjectResponse editProject(Long projectId,EditProjectInput editProjectInput) {
        //Por el momenton no reasigna al espaciodetrabajo => workspaceId no edit sisabe miparcero

        //buscar proyecto
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ItemNotFoundException("Proyecto no encontrado"));

        //validar nombre y actualizar
        projectMapper.updateProjectFromDto(editProjectInput,project);

        if(editProjectInput.getTaskIds() != null){
            List<Task> tasks = tasksRepository.findAllById(editProjectInput.getTaskIds());

            //setear el cambio en la relacion
            tasks.forEach(task-> task.setProject(project));
            project.setTasks(tasks);

        }

        projectRepository.save(project);

        Long count = tasksRepository.countByProjectId(project.getId());

        return projectMapper.toResponseWithTasksCount(project,count);
    }

    @Override
    public Boolean deleteProject(Long projectId) {

        projectRepository.deleteById(projectId);

        return true;
    }


}


