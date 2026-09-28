package com.back.repositories;

import com.back.entities.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<Workspace,UUID > {

    boolean existsByNameAndOwner_Id(String name, UUID ownerId);

    @Query("""
    SELECT DISTINCT w
    FROM Workspace w
    LEFT JOIN FETCH w.members wm
    LEFT JOIN FETCH wm.user u
    WHERE w.id = :id
    """)
    Optional<Workspace> findByIdWithMembers(@Param("id") UUID id);


}
