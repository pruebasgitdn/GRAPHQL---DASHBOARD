package com.back.services;

import com.back.entities.dto.TaskAssigneeResponse;

import java.util.List;
import java.util.UUID;

public interface TaskAssigneeService {

    Boolean deleteAssignationByTaskId(Long taskId,UUID currentUser);

    TaskAssigneeResponse createAssignation(Long taskId, UUID user_assign, UUID currentUser);

    List<TaskAssigneeResponse> assignationsByUserId(UUID owner);

    List<TaskAssigneeResponse> assignationsByTaskId(Long taskId);

    List<TaskAssigneeResponse> allAssignations();



}
