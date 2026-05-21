package com.back.repositories;

import com.back.entities.WorkspaceInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WorkspaceInvitationRepository extends JpaRepository<WorkspaceInvitation,UUID> {

    Optional<WorkspaceInvitation> findByToken(String token);

}
