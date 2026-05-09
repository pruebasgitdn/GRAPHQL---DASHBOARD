package com.back.services;

import com.back.entities.Task;
import com.back.entities.dto.CreateTaskInput;
import com.back.entities.dto.EditTaskInput;
import com.back.entities.dto.TaskResponse;

import java.util.List;
import java.util.UUID;

public interface TasksService {

    TaskResponse createTask(CreateTaskInput createTaskInput,UUID creator);

    List<TaskResponse> findAllTasks();

    TaskResponse getTask(Long id);

    List<TaskResponse> findAllByProjectId(Long projectId);

    TaskResponse editTask(Long taskId, EditTaskInput editTaskInput);

    Boolean deleteTask(Long taskId);
}
