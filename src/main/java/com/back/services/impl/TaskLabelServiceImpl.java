package com.back.services.impl;

import com.back.entities.Task;
import com.back.entities.TaskLabel;
import com.back.entities.dto.TaskLabelResponse;
import com.back.enums.TaskLabelType;
import com.back.exceptions.AlreadyExistException;
import com.back.repositories.TaskLabelRepository;
import com.back.services.TaskLabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TaskLabelServiceImpl implements TaskLabelService {

    private final TaskLabelRepository taskLabelRepository;

    @Override
    public List<TaskLabelResponse> createManyTaskLabel(Task task,  List<TaskLabelType> labels) {


        if(task == null || labels == null || labels.isEmpty()){
            throw new IllegalArgumentException("Ingresa todos los argumentos");
        }
        if (labels.size() != new HashSet<>(labels).size()) {
            throw new IllegalArgumentException("Labels duplicados");
        }

        labels.forEach(label -> {
            if (taskLabelRepository.existsByTask_IdAndLabel(task.getId(), label)) {
                throw new AlreadyExistException("La etiqueta " + label + " ya está asociada a la tarea");
            }
        });

        List<TaskLabel> entities = labels.stream()
                .map(label -> TaskLabel.builder()
                        .task(task)
                        .label(label)
                        .build()
                )
                .toList();

        List<TaskLabel> saved = taskLabelRepository.saveAll(entities);

        //Añadir al task
        saved.forEach(task.getLabels()::add);

        return saved.stream()
                .map(tl -> TaskLabelResponse.builder()
                        .id(tl.getId())
                        .label(tl.getLabel())
                        .build()
                )
                .toList();

    }

}
