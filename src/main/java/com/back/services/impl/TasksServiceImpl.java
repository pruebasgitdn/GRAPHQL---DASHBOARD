package com.back.services.impl;

import com.back.entities.Task;
import com.back.entities.dto.CreateTaskInput;
import com.back.repositories.TasksRepository;
import com.back.services.TasksService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TasksServiceImpl implements TasksService {

    private final TasksRepository tasksRepository;


    @Override
    public Task createTask(CreateTaskInput createTaskInput) {
        return null;
    }
}
