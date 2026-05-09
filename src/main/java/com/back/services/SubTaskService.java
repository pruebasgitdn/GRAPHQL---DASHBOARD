package com.back.services;

import com.back.entities.dto.CreateSubTask;
import com.back.entities.dto.SubTaskResponse;

import java.util.List;

public interface SubTaskService {

    List<SubTaskResponse> createSubTasks(Long taskId, List<CreateSubTask> input);

    List<SubTaskResponse> getSubTasksByTaskId(Long taskId);

}
