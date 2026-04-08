package com.back.repositories;

import com.back.entities.TaskAssignee;
import com.back.entities.dto.TaskAssigneeResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskAssigneeRepository extends JpaRepository<TaskAssignee,Long> {

    void deleteByTaskId(Long taskId);

    List<TaskAssignee> findAllByUserId(UUID userId);

    List<TaskAssignee> findAllByTaskId(Long taskId);


}
