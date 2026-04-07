package com.back.repositories;

import com.back.entities.Project;
import com.back.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project,Long> {

    List<Task> findAllById(Long id);

    @Override
    Optional<Project> findById(Long id);

    List<Project> findAllByWorkspaceId(UUID id);

    boolean existsByNameAndWorkspaceId(String name, UUID workspaceId);

    boolean existsByName(String name);

    Long countByWorkspaceId(UUID id);


}
