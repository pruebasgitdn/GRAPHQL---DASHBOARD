package com.back.services.impl;

import com.back.entities.WorkspaceMember;
import com.back.entities.dto.WorkspaceMembersResponse;
import com.back.enums.Role;
import com.back.repositories.WorkspaceMemberRepository;
import com.back.services.WorkspaceMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceMemberServiceImpl implements WorkspaceMemberService {

    private final WorkspaceMemberRepository workspaceMemberRepository;

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
}
