package com.back.services;

import com.back.entities.Task;
import com.back.entities.dto.TaskLabelResponse;
import com.back.enums.TaskLabelType;

import java.util.List;

public interface TaskLabelService {

    List<TaskLabelResponse> createManyTaskLabel(Task task, List<TaskLabelType> label);

}
