package com.back.repositories;

import com.back.entities.Project;
import com.back.entities.SubTask;
import com.back.entities.Task;
import com.back.entities.dto.TaskStats;
import com.back.entities.dto.TaskStatsProjection;
import com.back.enums.TaskPriority;
import com.back.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TasksRepository extends JpaRepository<Task,Long> {

    @Override
    Optional<Task> findById(Long id);

    boolean existsByTitleAndProjectId(String title, Long projectId);

    List<Task> findAllByProjectId(Long projectId);

    //El collection es una estructura de datos q me permite
    //Agrupar multiples elementos en una unidad
    List<Task> findAllByProjectIdIn(Collection<Long> projectId);

    Long countByProjectId(Long projectId);


    //al ser left los del task donde se cumple la condicion
    //pero si necesito ambos o datos de esa entidad un JOIN
    @Query("""
    SELECT t FROM Task t
    LEFT JOIN FETCH t.labels
    WHERE t.id = :id
    """)
    Optional<Task> findByIdWithLabels(Long id);


    @Modifying
    @Transactional
    @Query("""
    UPDATE Task k
    SET k.title = :title
    WHERE k.id = :id
    """)
    int updateTitleById(Long id, String title);

    @Modifying
    @Transactional
    @Query("""
    UPDATE Task k
    SET k.description = :description
    WHERE k.id = :id
    """)
    int updateDescriptionById(Long id, String description);

    @Modifying
    @Transactional
    @Query("""
    UPDATE Task k
    SET k.priority = :priority
    WHERE k.id = :id
    """)
    int updatePriorityById(Long id, TaskPriority priority);

    @Modifying
    @Transactional
    @Query("""
    UPDATE Task k
    SET k.status = :status
    WHERE k.id = :id
    """)
    int updateStatusById(Long id, TaskStatus status);

    @Query("""
            SELECT t.status, COUNT(t)
            FROM Task t
            WHERE t.project.workspace.id = :workspaceId
            GROUP BY t.status
            """)
    List<Object[]> getTaskPriorityDistribution(
            UUID workspaceId
    );

    @Query("""
            SELECT t.priority, COUNT(t)
            FROM Task t
            WHERE t.project.workspace.id = :workspaceId
            GROUP BY t.priority
            """)
    List<Object[]> getTaskStatusDistribution(
            UUID workspaceId
    );

    @Query("""
            SELECT COUNT(t)
            FROM Task t
            WHERE t.project.workspace.id = :workspaceId
            """)
    Long countByWorkspaceId(UUID workspaceId);


    @Query("""
            SELECT COUNT(t)
            FROM Task t
            WHERE t.project.workspace.id = :workspaceId
            AND t.priority = :priority
            """)
    Long countByWorkspaceIdAndPriority(
            UUID workspaceId,
            TaskPriority priority
    );

    @Query("""
                SELECT
                    COUNT(t) as totalTasks,
                 COALESCE(SUM(CASE WHEN t.priority = :todo THEN 1 ELSE 0 END), 0) as todoTasks,
                 COALESCE(SUM(CASE WHEN t.priority = :inProgress THEN 1 ELSE 0 END), 0) as inProgressTasks,
                 COALESCE(SUM(CASE WHEN t.priority = :done THEN 1 ELSE 0 END), 0) as doneTasks
                FROM Task t
                WHERE t.project.workspace.id = :workspaceId
            """)
    TaskStatsProjection getTaskStats(
            UUID workspaceId,
            TaskPriority todo,
            TaskPriority inProgress,
            TaskPriority done
    );

}
