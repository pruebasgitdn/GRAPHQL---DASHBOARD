package com.back.services;

import com.back.entities.dto.TaskAssigneeResponse;

import java.util.UUID;

public interface TaskAssigneeService {

    Boolean deleteAssignation(Long taskId);

    TaskAssigneeResponse createAssignation(Long taskId, UUID user_assign, UUID currentUser);

}
