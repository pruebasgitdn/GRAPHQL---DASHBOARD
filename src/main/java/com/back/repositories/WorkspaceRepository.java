package com.back.repositories;

import com.back.entities.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<Workspace,UUID > {

    boolean existsByNameAndOwner_Id(String name, UUID ownerId);
}
