package com.back.services.impl;

import com.back.entities.Project;
import com.back.entities.Task;
import com.back.entities.User;
import com.back.entities.Workspace;
import com.back.entities.dto.*;
import com.back.entities.mappers.ProjectMapper;
import com.back.entities.mappers.TasksMapper;
import com.back.entities.mappers.UserMapper;
import com.back.entities.mappers.WorkspaceMapper;
import com.back.exceptions.AlreadyExistException;
import com.back.exceptions.InvalidCredentialsException;
import com.back.exceptions.ItemNotFoundException;
import com.back.repositories.ProjectRepository;
import com.back.repositories.TasksRepository;
import com.back.repositories.UserRepository;
import com.back.repositories.WorkspaceRepository;
import com.back.services.ProjectService;
import com.back.services.UserService;
import com.back.services.WorkspaceMemberService;
import com.back.services.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    private final TasksRepository tasksRepository;

    private final ProjectMapper projectMapper;
    private final WorkspaceMemberService workspaceMemberService;



    @Override
    public ProjectResponse createProject(CreateProjectInput projectInput) {

        //Encontrar workspace
        Workspace workspace = workspaceRepository.findById(projectInput.getWorkspaceId()).orElseThrow(()->{
            throw new ItemNotFoundException("Worksapce no encontrado");
        });

        //Encontrar owner
        User owner = userRepository.findById(projectInput.getOwnerId()).orElseThrow(()->{
            throw new ItemNotFoundException("Creador no encontrado");
        });

        if(!workspaceMemberService.isMember(workspace.getId(),owner.getId())){
            throw  new RuntimeException("No eres miembro, no tienes permiso para esta accion (crear proyecto)");
        }


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

        if(projectInput.getDueDate().isBefore(projectInput.getStartDate())){
            throw new RuntimeException("Ingresa un rango de fechas valido");
        }


        //Crear pryecto,guardar y retornar
        Project project = Project
                .builder()
                .name(projectInput.getName())
                .description(projectInput.getDescription())
                .workspace(workspace)
                .owner(owner)
                .status(projectInput.getStatus())
                .startDate(projectInput.getStartDate())
                .dueDate(projectInput.getDueDate())
                .tasks(tasksListToAdd)
                .isArchived(false)
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

    @Cacheable(value = "project", key = "#id")
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
    @Caching(evict = {

            @CacheEvict(value = "project", key = "#id"),
            //@CacheEvict(value = "workspaces", key = "'all'")
    })
    public ProjectResponse editProject(Long id,EditProjectInput editProjectInput) {

        //buscar proyecto
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Proyecto no encontrado"));

        if(editProjectInput.getDueDate() != null &&
                editProjectInput.getStartDate() != null){

            if (editProjectInput.getStartDate().isAfter(editProjectInput.getDueDate())) {
                throw new RuntimeException("Ingrese un rango de fecha valido");
            }

        }



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
    @CacheEvict(value = "project", key = "#id")
    public Boolean deleteProject(Long id) {

        projectRepository.deleteById(id);

        //TODO: emitir notificacion o algo

        return true;
    }

    //TODO EDITAR NOMBRE Y/O DESCRIP POR SEPARADO ? O JUNTO ?=
    //MANDARLO tododesde del clieente un solo form mejor


}


