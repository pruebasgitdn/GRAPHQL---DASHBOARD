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
import com.back.services.TasksService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TasksServiceImpl implements TasksService {

    private final TasksRepository tasksRepository;
    private final ProjectRepository projectRepository;
    private final TaskAssigneeRepository taskAssigneeRepository;

    private final TasksMapper tasksMapper;
    private final ProjectMapper projectMapper;


    @Override
    public TaskResponse createTask(CreateTaskInput createTaskInput) {

        //verificar
        if(tasksRepository.existsByTitleAndProjectId(createTaskInput.getTitle(),createTaskInput.getProjectId())){
            throw new AlreadyExistException("Ya existe una tarea con ese nombre en este proyecto");
        }

        //encontrar proyecto
        Project project = projectRepository.findById(createTaskInput.getProjectId())
                .orElseThrow(()-> {throw new ItemNotFoundException("No se encontro dicho proyecto");});


        Task taskToSave = Task.builder()
                .title(createTaskInput.getTitle())
                .description(createTaskInput.getDescription())
                .project(project)
                .priority(createTaskInput.getPriority())
                .status(createTaskInput.getStatus())
                .build();

        tasksRepository.save(taskToSave);

        return TaskResponse.builder()
                .id(taskToSave.getId())
                .title(taskToSave.getTitle())
                .description(taskToSave.getDescription())
                .project(projectMapper.toResponseWithoutCount(taskToSave.getProject()))
                .priority(taskToSave.getPriority())
                .status(taskToSave.getStatus())
                .build();
    }

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
    public TaskResponse getTask(Long id) {


        Task task = tasksRepository.findById(id).orElseThrow(()->{
            throw  new ItemNotFoundException("No se se encontro tarea por el id: "+id);
        });

        return tasksMapper.toResponse(task);
    }

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
