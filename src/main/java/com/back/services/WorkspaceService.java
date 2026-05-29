package com.back.services;


import com.back.entities.Workspace;
import com.back.entities.dto.*;

import java.util.List;
import java.util.UUID;

public interface WorkspaceService {

    WorkspaceResponse createWorkspace(CreateWorkspaceInput workspaceInput, UUID owner_id);

    List<WorkspaceResponse> findAll();

    WorkSpaceDetailResponse findById(UUID id);

    WorkspaceResponse addMemberToWorkspace(List<UUID> users, UUID workspace_id,UUID owner_id);

    //sin verificar el owner pq se añade cuando el invitado acepte en en el link
    WorkspaceResponse addMemberToWorkspaceAfterLink(UUID userId, UUID workspace_id);

    WorkspaceResponse removeMembersFromWorkspace(List<UUID> users, UUID workspace_id,UUID owner_id);

    Boolean removeWorkspace( UUID workspace_id,UUID owner_id);

    WorkspaceResponse editWorkspace(EditWorkspaceInput input,UUID id,UUID ownerId);
}
