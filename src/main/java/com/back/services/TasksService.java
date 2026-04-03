package com.back.services;

import com.back.entities.Task;
import com.back.entities.dto.CreateTaskInput;

public interface TasksService {

    Task createTask(CreateTaskInput createTaskInput);

}
