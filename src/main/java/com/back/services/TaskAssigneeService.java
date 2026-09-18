package com.back.services;

import com.back.entities.dto.MyTasksDto;
import com.back.entities.dto.TaskAssigneeResponse;
import com.back.entities.dto.TaskAssigneeResponseLblPrName;

import java.util.List;
import java.util.UUID;

public interface TaskAssigneeService {

    Boolean deleteAssignationByTaskId(Long taskId,UUID currentUser,UUID workspaceId);

    TaskAssigneeResponse createAssignation(Long taskId, UUID user_assign, UUID currentUser);

    List<TaskAssigneeResponse> createMultipleAssignations(Long taskId, List<UUID> userIds, UUID currentUser,UUID workspaceId);

    List<TaskAssigneeResponseLblPrName> assignationsByUserId(UUID owner);

    List<TaskAssigneeResponse> assignationsByTaskId(Long taskId);

    List<TaskAssigneeResponse> allAssignations();

    List<MyTasksDto> assignationsByUserIdRMX(UUID owner);


}
