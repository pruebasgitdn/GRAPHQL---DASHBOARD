package com.back.services.impl;

import com.back.entities.WorkspaceMember;
import com.back.entities.dto.WorkspaceMembersResponse;
import com.back.enums.Role;
import com.back.exceptions.ItemNotFoundException;
import com.back.repositories.UserRepository;
import com.back.repositories.WorkspaceMemberRepository;
import com.back.repositories.WorkspaceRepository;
import com.back.services.WorkspaceMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceMemberServiceImpl implements WorkspaceMemberService {

    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;


    @Override
    public boolean isAdminOrOwner(UUID user_id, UUID workspace_id) {
        return workspaceMemberRepository.existsByWorkspace_IdAndUser_IdAndRoleIn(
                workspace_id,
                user_id,
                List.of(Role.ADMIN,Role.OWNER)
        );
    }

    @Override
    public List<WorkspaceMember> findAll() {
        return workspaceMemberRepository.findAll();
    }

    @Override
    public List<WorkspaceMember> getUserWorkspaces(UUID user_id) {

        userRepository.findById(user_id)
                .orElseThrow(() -> new ItemNotFoundException("Usuario no encontrado"));

        return workspaceMemberRepository.findAllByUserId(user_id);
    }

    @Override
    public List<WorkspaceMember> getWorkspaceUsers(UUID workspace_id) {

        workspaceRepository.findById(workspace_id)
                .orElseThrow(() -> new ItemNotFoundException("Workspace no encontrado"));

        return workspaceMemberRepository.findAllByWorkspaceId(workspace_id);
    }
}
