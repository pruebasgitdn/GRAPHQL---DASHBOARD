package com.back.repositories;

import com.back.entities.Project;
import com.back.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TasksRepository extends JpaRepository<Task,Long> {

    @Override
    Optional<Task> findById(Long id);

    List<Task> findAllById(Long id);

    boolean existsByTitle(String title);

    boolean existsByTitleAndProjectId(String title, Long projectId);

    List<Task> findAllByProjectId(Long projectId);

    Long countByProjectId(Long projectId);

    void deleteByProjectId(Long projectId);
}
