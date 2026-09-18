package com.back.repositories;

import com.back.entities.Project;
import com.back.entities.SubTask;
import com.back.entities.Task;
import com.back.entities.dto.*;
import com.back.enums.TaskPriority;
import com.back.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
            SELECT t.priority, COUNT(t)
            FROM Task t
            WHERE t.project.workspace.id = :workspaceId
            GROUP BY t.priority
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




    @Query("""
            SELECT new com.back.entities.dto.TaskTrendCL(
                t.createdAt,
                t.priority,
                COUNT(t.id)
            )
            FROM Task t
            WHERE t.project.workspace.id = :workspaceId
            AND t.createdAt BETWEEN :startDate AND :endDate
            GROUP BY t.createdAt, t.priority
            ORDER BY t.createdAt
            """)
    List<TaskTrendCL> getTaskTrends(
            @Param("workspaceId") UUID workspaceId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    @Query("""
            SELECT t
            FROM Task t
            WHERE t.project.workspace.id = :workspaceId
            AND t.createdAt BETWEEN :startDate AND :endDate
            """)
    List<Task> test(
            UUID workspaceId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );


    @Query("""
SELECT t
FROM Task t
WHERE t.project.workspace.id = :workspaceId
""")
    List<Task> testdos(UUID workspaceId);


    @Query("""
SELECT t
FROM Task t
WHERE t.createdAt BETWEEN :startDate AND :endDate
""")
    List<Task> testtres(
            LocalDateTime startDate,
            LocalDateTime endDate
    );


    //Para los proxmos 30 dias tasktrends
    @Query(value = """
             SELECT
                   DATE(t.created_at) AS day,
                   t.priority,
                   COUNT(*) AS total
               FROM task t
               JOIN project p ON p.id = t.project_id
               WHERE p.workspace_id = :workspaceId
                 AND t.created_at BETWEEN :startDate AND :endDate
               GROUP BY DATE(t.created_at), t.priority
               ORDER BY day
            """, nativeQuery = true)
    List<TaskTrendProjection> perramalparida(
            UUID workspaceId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );


    @Query("""
             SELECT t
             FROM Task t
             WHERE t.project.workspace.id = :workspaceId
             AND t.dueDate BETWEEN :today AND :nextWeek
             ORDER BY t.dueDate ASC
            """)
    List<Task> findUpcomingTasks(UUID workspaceId, LocalDate today,
                                 LocalDate nextWeek);


    @Query("""
    SELECT DISTINCT t
    FROM Task t
    LEFT JOIN FETCH t.project
    LEFT JOIN FETCH t.labels
    WHERE t.id = :taskId
""")
    Optional<Task> findByIdWithProjectAndLabels(
            @Param("taskId") Long taskId
    );

    @Query("""
    SELECT t
    FROM Task t
    LEFT JOIN FETCH t.labels
    WHERE t.id = :taskId
""")
    Optional<Task> findByIdWithProjectAndLabelZZZ(
            @Param("taskId") Long taskId
    );

    @Query(value = "SELECT current_database()", nativeQuery = true)
    String currentDatabase();

    @Query(value = "SELECT inet_server_addr()", nativeQuery = true)
    String inet_server_addr();

    @Query(value = "SELECT COUNT(*) FROM task", nativeQuery = true)
    Long nancy();




//    @Query("""
//            SELECT t
//            FROM Task t
//            WHERE t.project.workspace.id = :workspaceId
//            AND t.dueDate BETWEEN CURRENT_DATE AND :endDate
//            ORDER BY t.dueDate ASC
//            """)
//    List<Task> findUpcomingTasks(
//            @Param("workspaceId") UUID workspaceId,
//            @Param("endDate") LocalDate endDate,
//            Pageable pageable
//    );


}
