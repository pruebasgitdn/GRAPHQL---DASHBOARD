package com.back.repositories;


import com.back.entities.WorkspaceMember;
import com.back.entities.dto.UserTaskStatsDTO;
import com.back.entities.dto.UserTaskStatsDTO;
import com.back.entities.dto.WorkspaceMemberResponse;
import com.back.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
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

    void deleteByWorkspace_IdAndUser_Id(UUID workspaceId, UUID userId);

    @Query("SELECT wm.user.id FROM WorkspaceMember wm WHERE wm.workspace.id = :workspaceId")
    Set<UUID> findUserIdsByWorkspaceId(UUID workspaceId);



    //WHERE wm.workspace.id = :workspaceId == solo miembros del espacio
    //p.workspace.id = :workspaceId == solo tareas del espacio
    @Query(
            """
    SELECT new com.back.entities.dto.UserTaskStatsDTO(
        u.id,
        u.email,
        u.name,
        SUM(CASE WHEN t.priority = 'TODO'
         AND p.workspace.id = :workspaceId
         THEN 1 ELSE 0 END),
         
        SUM(CASE WHEN t.priority = 'DONE'
        AND p.workspace.id = :workspaceId                 
         THEN 1 ELSE 0 END),
        
        
        SUM(CASE WHEN t.priority = 'BLOCKED'
              AND p.workspace.id = :workspaceId
         THEN 1 ELSE 0 END),
        
        
        SUM(CASE WHEN t.priority = 'IN_PROGRESS' 
             AND p.workspace.id = :workspaceId     
        THEN 1 ELSE 0 END)
    )
    FROM WorkspaceMember wm
    JOIN wm.user u
    LEFT JOIN TaskAssignee ta
        ON ta.user.id = u.id
    LEFT JOIN ta.task t
    LEFT JOIN t.project p
    WHERE wm.workspace.id = :workspaceId
    GROUP BY u.id, u.email, u.name
    """
    )
    List<UserTaskStatsDTO> findMemberTaskStats(
            @Param("workspaceId") UUID workspaceId
    );




}
