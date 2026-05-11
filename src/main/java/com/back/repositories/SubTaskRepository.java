package com.back.repositories;


import com.back.entities.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface SubTaskRepository extends JpaRepository<SubTask,Long> {

    //sql generado
    // WHERE task_id IN (5, 6, 7)
    List<SubTask> findAllByTaskIdIn(Collection<Long> taskIds);

    List<SubTask> findAllByTaskId(Long taskId);


}
