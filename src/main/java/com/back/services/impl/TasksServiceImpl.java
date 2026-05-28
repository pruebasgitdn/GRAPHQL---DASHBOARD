package com.back.services.impl;

import com.back.entities.Project;
import com.back.entities.Task;
import com.back.entities.User;
import com.back.entities.dto.CreateTaskInput;
import com.back.entities.dto.EditTaskInput;
import com.back.entities.dto.TaskResponse;
import com.back.entities.mappers.ProjectMapper;
import com.back.entities.mappers.TasksMapper;
import com.back.exceptions.AlreadyExistException;
import com.back.exceptions.ItemNotFoundException;
import com.back.exceptions.UserNotFoundException;
import com.back.repositories.ProjectRepository;
import com.back.repositories.TaskAssigneeRepository;
import com.back.repositories.TasksRepository;
import com.back.repositories.UserRepository;
import com.back.services.TaskLabelService;
import com.back.services.TasksService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TasksServiceImpl implements TasksService {

    private final TasksRepository tasksRepository;
    private final ProjectRepository projectRepository;
    private final TaskAssigneeRepository taskAssigneeRepository;
    private final UserRepository userRepository;

    private final TasksMapper tasksMapper;
    private final ProjectMapper projectMapper;
    private final TaskLabelService taskLabelService;


    @Transactional(readOnly = false)
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


        Task taskToSave = Task.builder()
                .title(createTaskInput.getTitle())
                .description(createTaskInput.getDescription())
                .project(project)
                .priority(createTaskInput.getPriority())
                .status(createTaskInput.getStatus())
                .actualHours(createTaskInput.getActualHours())
                .completedAt(createTaskInput.getCompletedAt())
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

    @Cacheable(value = "task", key = "#id")
    @Transactional(readOnly = true)
    @Override
    public TaskResponse getTask(Long id) {


        Task task = tasksRepository.findById(id).orElseThrow(()->{
            throw  new ItemNotFoundException("No se se encontro tarea por el id: "+id);
        });

        return tasksMapper.toResponse(task);
    }


    @Cacheable(value = "tasksByProject", key = "#projectId")
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
    public TaskResponse editTask(Long taskId, EditTaskInput editTaskInput) {

        //encontrar por id
        Task task = tasksRepository.findById(taskId).orElseThrow(()->{
            throw  new ItemNotFoundException("No se se encontro tarea por el id: "+taskId);
        });


        tasksMapper.updateTaskFromDto(editTaskInput,task);

        tasksRepository.save(task);

        return  tasksMapper.toResponse(task);

    }

    @Override
    public Boolean deleteTask(Long taskId) {

        tasksRepository.deleteById(taskId);

        return true;
    }
}
