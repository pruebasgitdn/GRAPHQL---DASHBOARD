package com.back.services.impl;
import com.back.entities.SubTask;
import com.back.entities.Task;
import com.back.entities.dto.CreateSubTask;
import com.back.entities.dto.SubTaskResponse;
import com.back.entities.mappers.SubTaskMapper;
import com.back.exceptions.ItemNotFoundException;
import com.back.repositories.SubTaskRepository;
import com.back.repositories.TasksRepository;
import com.back.services.SubTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SubTaskServiceImpl implements SubTaskService {

    private final TasksRepository tasksRepository;
    private final SubTaskRepository subTaskRepository;
    private final SubTaskMapper subTaskMapper;

    @Override
    public List<SubTaskResponse> createSubTasks(Long taskId, List<CreateSubTask> inputs) {

        Task task = tasksRepository.findById(taskId).
                orElseThrow(()->{
                    throw new ItemNotFoundException("No se encontró la tarea. este item no existe");
                });


        if(inputs == null || inputs.isEmpty()){
            throw new RuntimeException("Lista vacía");
        }

        List<SubTask> subTasks = inputs.stream()
                .map(input -> SubTask.builder()
                        .title(input.getTitle())
                        .completed(input.getCompleted())
                        .task(task)
                        .build()
                )
                .toList();

        List<SubTask> saved = subTaskRepository.saveAll(subTasks);


          return saved.stream().map((
                sv ->  subTaskMapper.toResponse(sv)
                )).collect(Collectors.toList());

    }

    @Override
    public List<SubTaskResponse> getSubTasksByTaskId(Long taskId) {
        List<SubTask> subTasks = subTaskRepository.findByTaskId(taskId);

        if(subTasks.isEmpty()){
            throw new ItemNotFoundException("No se encontraron subtareas asociadas por el momento");
        }

        return subTasks.stream().map((
                mp-> subTaskMapper.toResponse(mp)
                )).collect(Collectors.toList());
    }

    @Override
    public List<SubTaskResponse> getAllSubTasks() {

        List<SubTask> subTasks = subTaskRepository.findAll();

        if(subTasks.isEmpty()){
            throw new ItemNotFoundException("No hay subtareas por el momento");
        }

        return subTasks.stream()
                .map(p->
                    subTaskMapper.toResponse(p)
                ).toList();
    }

}
