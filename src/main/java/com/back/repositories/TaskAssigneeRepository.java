package com.back.repositories;

import com.back.entities.TaskAssignee;
import com.back.entities.dto.TaskAssigneeResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskAssigneeRepository extends JpaRepository<TaskAssignee,Long> {

    void deleteByTaskId(Long taskId);

}
