package com.back.services;

import com.back.entities.Task;
import com.back.entities.dto.*;
import com.back.enums.TaskPriority;
import com.back.enums.TaskStatus;

import java.util.List;
import java.util.UUID;

public interface TasksService {

    TaskResponse createTask(CreateTaskInput createTaskInput,UUID creator);

    TaskResponse createTaskTest(CreateTaskInputTest createTaskInput, UUID creator);

    List<TaskResponse> findAllTasks();

    TaskLabelProjectDto findTaskWithLabelsAndProjectName(Long id);
    TaskLabelProjectDto findTaskWithLabelsAndProjectNameZZZ(Long id);


    TaskResponse getTask(Long id);

    List<TaskResponse> findAllByProjectId(Long projectId);

    TaskResponse editTask(Long taskId, EditTaskInput editTaskInput);

    Boolean deleteTask(Long taskId);

    Boolean editTaskTitle(String newTitle, Long id);

    Boolean editTaskDescription(String newDescription, Long id);

    Boolean editTaskStatus(TaskStatus status,Long id);

    Boolean editTaskPriority(TaskPriority priority,Long id);

}
