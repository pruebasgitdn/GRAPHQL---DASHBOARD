package com.back.repositories;


import com.back.entities.WorkspaceMember;
import com.back.entities.dto.WorkspaceMemberResponse;
import com.back.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember,Long> {

    long countByWorkspaceId(UUID id);

    boolean existsByWorkspace_IdAndUser_IdAndRoleIn(
            UUID workspaceId,
            UUID userId,
            List<Role> roles
    );

    boolean existsByWorkspace_IdAndUser_Id(UUID workspaceId, UUID userId);

    List<WorkspaceMember> findAllByUserId(UUID id);

    List<WorkspaceMember> findAllByWorkspaceId(UUID id);


}
