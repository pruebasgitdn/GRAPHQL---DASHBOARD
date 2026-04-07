package com.back.services;

import com.back.entities.WorkspaceMember;
import com.back.entities.dto.WorkspaceMemberResponse;
import com.back.entities.dto.WorkspaceMembersResponse;

import java.util.List;
import java.util.UUID;

public interface WorkspaceMemberService {

    boolean isAdminOrOwner(UUID user_id, UUID workspace_id);

    List<WorkspaceMemberResponse> findAll();

    List<WorkspaceMemberResponse> getUserWorkspaces(UUID user_id);

    List<WorkspaceMemberResponse> getWorkspaceUsers(UUID workspace_id);


}
