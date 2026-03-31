package com.back.services;


import com.back.entities.Workspace;
import com.back.entities.dto.CreateProjectInput;
import com.back.entities.dto.CreateWorkspaceInput;
import com.back.entities.dto.WorkspaceResponse;

import java.util.List;
import java.util.UUID;

public interface WorkspaceService {

    WorkspaceResponse createWorkspace(CreateWorkspaceInput workspaceInput, UUID owner_id);

    List<WorkspaceResponse> findAll();

    WorkspaceResponse findById(UUID id);

    WorkspaceResponse addMemberToWorkspace(List<UUID> users, UUID workspace_id,UUID owner_id);
}
