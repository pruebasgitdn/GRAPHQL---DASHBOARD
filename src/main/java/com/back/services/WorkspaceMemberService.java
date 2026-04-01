package com.back.services;

import com.back.entities.WorkspaceMember;
import com.back.entities.dto.WorkspaceMembersResponse;

import java.util.List;
import java.util.UUID;

public interface WorkspaceMemberService {

    boolean isAdminOrOwner(UUID user_id, UUID workspace_id);

    List<WorkspaceMember> findAll();

    List<WorkspaceMember> getUserWorkspaces(UUID user_id);

    List<WorkspaceMember> getWorkspaceUsers(UUID workspace_id);


}
