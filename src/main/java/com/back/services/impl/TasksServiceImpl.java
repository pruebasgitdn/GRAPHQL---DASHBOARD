package com.back.services.impl;

import com.back.entities.*;
import com.back.entities.dto.*;
import com.back.entities.mappers.ProjectMapper;
import com.back.entities.mappers.TasksMapper;
import com.back.enums.TaskPriority;
import com.back.enums.TaskStatus;
import com.back.exceptions.AlreadyExistException;
import com.back.exceptions.ItemNotFoundException;
import com.back.exceptions.UserNotFoundException;
import com.back.repositories.*;
import com.back.services.TaskLabelService;
import com.back.services.TasksService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TasksServiceImpl implements TasksService {

    private final TasksRepository tasksRepository;
    private final ProjectRepository projectRepository;
    private final TaskAssigneeRepository taskAssigneeRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;


    private final TasksMapper tasksMapper;
    private final ProjectMapper projectMapper;
    private final TaskLabelService taskLabelService;
    private final CacheManager cacheManager;


    @Transactional
    @Override
    public TaskResponse createTask(CreateTaskInput createTaskInput,UUID owner_id) {


        //encontrar proyecto
        Project project = projectRepository.findById(createTaskInput.getProjectId())
                .orElseThrow(()-> {throw new ItemNotFoundException("No se encontro dicho proyecto");});

        //verificar
        if(tasksRepository.existsByTitleAndProjectId(createTaskInput.getTitle(),createTaskInput.getProjectId())){
            throw new AlreadyExistException("Ya existe una tarea con ese nombre en este proyecto");
        }

        //Ww
        User owner = userRepository.findById(owner_id).orElseThrow(()->{
            throw new ItemNotFoundException("Creador no encontrado");
        });

        //tLbale
        Task taskToSave = Task.builder()
                .title(createTaskInput.getTitle())
                .description(createTaskInput.getDescription())
                .project(project)
                .priority(createTaskInput.getPriority())
                .status(createTaskInput.getStatus())
                //.actualHours(createTaskInput.getActualHours())
                //.completedAt(createTaskInput.getCompletedAt())
                .estimatedHours(createTaskInput.getEstimatedHours())
                .isArchived(false)
                .dueDate(createTaskInput.getDueDate())
                .owner(owner)
                .build();

        Task savedTask = tasksRepository.save(taskToSave);

        //Label
        if (createTaskInput.getLabels() != null && !createTaskInput.getLabels().isEmpty()) {
            taskLabelService.createManyTaskLabel(savedTask, createTaskInput.getLabels());
        }
        //sincronizar los cambios realizados en las entidades gestionad
        //y la previa gestion de la creacion de los label en caso de haberlost
        tasksRepository.flush();

        Task fullTask = tasksRepository.findByIdWithLabels(savedTask.getId())
                .orElseThrow(()-> new ItemNotFoundException("No se encontraron resultados"));

        return tasksMapper.toResponse(fullTask);

    }

    @Override
    public TaskResponse createTaskTest(CreateTaskInputTest createTaskInput, UUID creator) {

        //1. fetch todos los wpsaces DE LOS QUE SE ES MIEMBRO
//        List<Workspace> workspaceList = workspaceRepository.findAll();
//        if(workspaceList.isEmpty()){
//            throw new ItemNotFoundException("No hay espacios de trabajo por el momento para poder crear una tarea");
//        }
        List<WorkspaceMember> wpmmm = workspaceMemberRepository.findAllByUserId(creator);
        if(wpmmm.isEmpty()){
            throw new ItemNotFoundException("No hay espacios de trabajo por el momento para poder crear una tarea");
        }

        System.out.println("wspaceList: "+wpmmm);

        //2. de esos wpsaces compararlo con el que mando el user
        UUID selectedWorkspace = wpmmm.stream()
                .filter(workspace ->
                        Objects.equals(
                                workspace.getWorkspace().getId(),
                                createTaskInput.getWorkspaceId()
                        )
                )
                .findFirst()
                .map(workspaceMember -> workspaceMember.getWorkspace().getId())
                .orElseThrow(() ->
                        new ItemNotFoundException("Espacio de trabajo no encontrado")
                );

        System.out.println("wspace Selected: "+selectedWorkspace);


        //3.Obtener los proyectos del espacio de trabajo seleccionado
        List<Project> relatedProjects = projectRepository.findAllByWorkspaceId(selectedWorkspace);
        if(relatedProjects.isEmpty()){
            throw new ItemNotFoundException("No hay proyectos asociados por el momento para poder crear una tarea");
        }
        System.out.println("related projects: "+relatedProjects);


        Project selectedProject = relatedProjects.stream()
                .filter(p ->
                        Objects.equals(
                                p.getId(),
                                createTaskInput.getProjectId()
                        )
                )
                .findFirst()
                .orElseThrow(() -> new ItemNotFoundException("Proyecto no encontrado"));

        System.out.println("sekected projects: "+selectedProject);

        User owner = userRepository.findById(creator).orElseThrow(()->{
            throw new ItemNotFoundException("Creador no encontrado");
        });

        //tLbale
        Task taskToSave = Task.builder()
                .title(createTaskInput.getTitle())
                .description(createTaskInput.getDescription())
                .project(selectedProject)
                .priority(createTaskInput.getPriority())
                .status(createTaskInput.getStatus())
                //.actualHours(createTaskInput.getActualHours())
                //.completedAt(createTaskInput.getCompletedAt())
                .estimatedHours(createTaskInput.getEstimatedHours())
                .isArchived(false)
                .dueDate(createTaskInput.getDueDate())
                .owner(owner)
                .build();

        Task savedTask = tasksRepository.save(taskToSave);

        //Label
        if (createTaskInput.getLabels() != null && !createTaskInput.getLabels().isEmpty()) {
            taskLabelService.createManyTaskLabel(savedTask, createTaskInput.getLabels());
        }
        //sincronizar los cambios realizados en las entidades gestionad
        //y la previa gestion de la creacion de los label en caso de haberlost
        tasksRepository.flush();

        Task fullTask = tasksRepository.findByIdWithLabels(savedTask.getId())
                .orElseThrow(()-> new ItemNotFoundException("No se encontraron resultados"));

        return tasksMapper.toResponse(fullTask);

    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskResponse> findAllTasks() {

        List<Task> tasks = tasksRepository.findAll();

        if(tasks.isEmpty()){
            throw new ItemNotFoundException("No se encontraron tareas");
        }

        return tasks.stream()
                .map(tasksMapper::toResponse)
                .toList();
    }

    @Override
    public TaskLabelProjectDto findTaskWithLabelsAndProjectName(Long taskId) {

        Task task = tasksRepository.findByIdWithProjectAndLabels(taskId)
                .orElseThrow(() ->
                        new ItemNotFoundException("No se encontraron registros")
                );


        System.out.println("TASK ID: " + task.getId());
        System.out.println("PROJECT: " + task.getProject());
        System.out.println("LABELS: " + task.getLabels());
        System.out.println("LABELS SIZE: " + task.getLabels().size());
        return tasksMapper.toTaskLabelProjectDto(task);


    }

    @Override
    public TaskLabelProjectDto findTaskWithLabelsAndProjectNameZZZ(Long id) {

        Task task = tasksRepository.findByIdWithProjectAndLabelZZZ(id)
                .orElseThrow(() ->
                        new ItemNotFoundException("No se encontraron registros")
                );


        System.out.println("TASK ID: " + task.getId());
        System.out.println("PROJECT: " + task.getProject());
        System.out.println("LABELS: " + task.getLabels());
        System.out.println("LABELS SIZE: " + task.getLabels().size());


        return tasksMapper.toTaskLabelProjectDto(task);
    }

    @Cacheable(value = "task", key = "#id")
    @Transactional(readOnly = true)
    @Override
    public TaskResponse getTask(Long id) {


        Task task = tasksRepository.findById(id).orElseThrow(()->{
            throw  new ItemNotFoundException("No se se encontro tarea por el id: "+id);
        });

        return tasksMapper.toResponse(task);
    }


    //@Cacheable(value = "tasksByProject", key = "#projectId")
    @Transactional(readOnly = true)
    @Override
    public List<TaskResponse> findAllByProjectId(Long projectId) {

        List<Task> tasks = tasksRepository.findAllByProjectId(projectId);

        if(tasks.isEmpty()){
            throw  new ItemNotFoundException("No se se encontro tarea asociadas al id del proyecto: "+projectId);
        }

        return tasks.stream()
                .map(tasksMapper::toResponse)
                .toList();


    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "task", key = "#id")}
    )
    public TaskResponse editTask(Long id, EditTaskInput editTaskInput) {

        //encontrar por id
        Task task = tasksRepository.findById(id).orElseThrow(()->{
            throw  new ItemNotFoundException("No se se encontro tarea por el id: "+id);
        });


        tasksMapper.updateTaskFromDto(editTaskInput,task);

        tasksRepository.save(task);

        return  tasksMapper.toResponse(task);

    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "task", key = "#id")}
    )
    public Boolean deleteTask(Long id) {


        Task task = tasksRepository.findById(id).orElseThrow(()->{
            throw  new ItemNotFoundException("No existe esa tarea");
        });


        //TODO: emitir notif o algo
        tasksRepository.deleteById(task.getId());

        UUID workspaceId = task.getProject().getWorkspace().getId();

        cacheManager.getCache("dashboard").evict(workspaceId);


        return true;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "task", key = "#id")}
    )
    public Boolean editTaskTitle(String newTitle, Long id) {

        if (newTitle == null || newTitle.length() > 70) {
            throw new IllegalArgumentException("El titulo no puede superar los 70 caracteres");
        }

        Task task = tasksRepository.findById(id).orElseThrow(()->{
            throw  new ItemNotFoundException("No existe esa tarea");
        });

       int updated =  tasksRepository.updateTitleById(id,newTitle);

        if(updated == 0){
            throw new ItemNotFoundException("Tarea no encontrada");
        }

        UUID workspaceId = task.getProject().getWorkspace().getId();

        cacheManager.getCache("dashboard").evict(workspaceId);

        return true;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "task", key = "#id")}
    )
    public Boolean editTaskDescription(String newDescription, Long id) {

        if (newDescription == null || newDescription.length() > 200) {
            throw new IllegalArgumentException("La descripción no puede superar los 200 caracteres");
        }

        Task task = tasksRepository.findById(id).orElseThrow(()->{
            throw  new ItemNotFoundException("No existe esa tarea");
        });

        int updated =  tasksRepository.updateDescriptionById(id,newDescription);


        if(updated == 0){
            throw new ItemNotFoundException("Tarea no encontrada");
        }
        UUID workspaceId = task.getProject().getWorkspace().getId();

        cacheManager.getCache("dashboard").evict(workspaceId);

        return true;

    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "task", key = "#id")}
    )
    public Boolean editTaskStatus(TaskStatus status,Long id) {

        if (status == null ) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }

        Task task = tasksRepository.findById(id).orElseThrow(()->{
            throw  new ItemNotFoundException("Tarea no encontrada");
        });

        int updated =  tasksRepository.updateStatusById(task.getId(),status);

        if(updated == 0){
            throw new ItemNotFoundException("Tarea no encontrada");
        }
        UUID workspaceId = task.getProject().getWorkspace().getId();

        cacheManager.getCache("dashboard").evict(workspaceId);

        return true;


    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "task", key = "#id"),
            //@CacheEvict(value = "dashboard", key = "#id")

    }
    )
    public Boolean editTaskPriority(TaskPriority priority,Long id) {

        if (priority == null ) {
            throw new IllegalArgumentException("La prioridad es obligatoria");
        }

        Task task = tasksRepository.findById(id).orElseThrow(()->{
            throw  new ItemNotFoundException("Tarea no encontrada");
        });

        int updated =  tasksRepository.updatePriorityById(task.getId(),priority);

        if(updated == 0){
            throw new ItemNotFoundException("Tarea no encontrada");
        }
        UUID workspaceId = task.getProject()
                .getWorkspace()
                .getId();
        
        cacheManager.getCache("dashboard").evict(workspaceId);

        return true;
    }
}
