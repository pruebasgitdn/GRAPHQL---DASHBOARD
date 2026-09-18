package com.back.repositories;

import com.back.entities.Task;
import com.back.entities.TaskAssignee;
import com.back.entities.dto.MyTasksDto;
import com.back.entities.dto.TaskAssigneeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TaskAssigneeRepository extends JpaRepository<TaskAssignee,Long> {

    void deleteByTaskId(Long taskId);

    List<TaskAssignee> findAllByUserId(UUID userId);

    List<TaskAssignee> findAllByTaskId(Long taskId);

    @Query("""
    SELECT ta.user.id
    FROM TaskAssignee ta
    WHERE ta.task.id = :taskId
    """)
    Set<UUID> findAssignedUserIdsByTaskId(Long taskId);

    boolean existsByUserIdAndTaskId(UUID userId, Long taskId);

    @Query("""
    SELECT t
    FROM TaskAssignee ta
    JOIN ta.task t
    JOIN t.project p
    JOIN p.workspace w
    WHERE ta.user.id = :userId
    """)
    List<Task> findUserTasks(@Param("userId") UUID userId);


}
