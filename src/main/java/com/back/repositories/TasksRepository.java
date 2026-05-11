package com.back.repositories;

import com.back.entities.Project;
import com.back.entities.SubTask;
import com.back.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TasksRepository extends JpaRepository<Task,Long> {

    @Override
    Optional<Task> findById(Long id);

//    List<Task> findAllById(Long id);
//    boolean existsByTitle(String title);

    boolean existsByTitleAndProjectId(String title, Long projectId);

    List<Task> findAllByProjectId(Long projectId);

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


    void deleteByProjectId(Long projectId);
}
