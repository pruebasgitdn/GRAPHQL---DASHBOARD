package com.back.repositories;

import com.back.entities.Project;
import com.back.entities.Task;
import com.back.enums.ProjectStatus;
import com.back.enums.TaskPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
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


    //El collection es una estructura de datos q me permite
    //Agrupar multiples elementos en una unidad
    List<Project> findAllByWorkspaceIdIn(Collection<UUID> workspaceId);


    @Query("""
            SELECT t.status, COUNT(t)
            FROM Project t
            WHERE t.workspace.id = :workspaceId
            GROUP BY t.status
            """)
    List<Object[]> getProjectStatusDistribution(
            UUID workspaceId
    );

    @Query("""
            SELECT COUNT(t)
            FROM Project t
            WHERE t.workspace.id = :workspaceId
            AND t.status = :status
            """)
    Long countByWorkspaceIdAndStatus(
            UUID workspaceId,
            ProjectStatus status
    );



    //Left va a priorizar los de la izq o primera q es Project y su select
    @Query("""
            SELECT 
                p.name,
                COUNT(t.id),
                SUM(CASE WHEN t.priority = 'DONE' THEN 1 ELSE 0 END)
            FROM Project p
            LEFT JOIN Task t ON t.project.id = p.id
            WHERE p.workspace.id = :workspaceId
            GROUP BY p.id, p.name
            """)
    List<Object[]> getWorkspaceProductivity(UUID workspaceId);




    List<Project> findTop5ByWorkspaceIdOrderByCreatedAtDesc(UUID workspaceId);



}
