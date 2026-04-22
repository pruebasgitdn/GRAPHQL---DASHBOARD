package com.back.services.impl;

import com.back.entities.WorkspaceMember;
import com.back.entities.dto.WorkspaceMemberResponse;
import com.back.entities.dto.WorkspaceMembersResponse;
import com.back.entities.mappers.WorkspaceMemberMapper;
import com.back.enums.Role;
import com.back.exceptions.ItemNotFoundException;
import com.back.repositories.ProjectRepository;
import com.back.repositories.UserRepository;
import com.back.repositories.WorkspaceMemberRepository;
import com.back.repositories.WorkspaceRepository;
import com.back.services.WorkspaceMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceMemberServiceImpl implements WorkspaceMemberService {

    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final WorkspaceMemberMapper workspaceMemberMapper;
    private final ProjectRepository projectRepository;

    @Override
    public boolean isAdminOrOwner(UUID user_id, UUID workspace_id) {
        return workspaceMemberRepository.existsByWorkspace_IdAndUser_IdAndRoleIn(
                workspace_id,
                user_id,
                List.of(Role.ADMIN,Role.OWNER)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<WorkspaceMemberResponse> findAll() {


        List<WorkspaceMember> workspaceMembers = workspaceMemberRepository.findAll();

        return workspaceMembers.stream()
                .map(workspaceMemberMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<WorkspaceMemberResponse> getUserWorkspaces(UUID user_id) {

        userRepository.findById(user_id)
                .orElseThrow(() -> new ItemNotFoundException("Usuario no encontrado"));

        List<WorkspaceMember> workspaceMembers =  workspaceMemberRepository.findAllByUserId(user_id);

        //contar miemrbso y projects para pasarlos
//        return workspaceMembers.stream()
//                .map(workspaceMemberMapper::toResponse)
//                .collect(Collectors.toList());

        return workspaceMembers.stream()
                .map(wm -> {
                    UUID workspaceId = wm.getWorkspace().getId();

                    Long projectCount = projectRepository.countByWorkspaceId(workspaceId);
                    Long memberCount = workspaceMemberRepository.countByWorkspaceId(workspaceId);

                    return workspaceMemberMapper.toResponseCount(wm, memberCount,projectCount);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkspaceMemberResponse> getWorkspaceUsers(UUID workspace_id) {

        workspaceRepository.findById(workspace_id)
                .orElseThrow(() -> new ItemNotFoundException("Workspace no encontrado"));

        List<WorkspaceMember> workspaceMembers = workspaceMemberRepository.findAllByWorkspaceId(workspace_id);

        return workspaceMembers.stream()
                .map(workspaceMemberMapper::toResponse)
                .collect(Collectors.toList());

    }

    @Override
    public boolean isMember(UUID workspaceId, UUID userId) {
        return workspaceMemberRepository.existsByWorkspace_IdAndUser_Id(workspaceId,userId);
    }
}
