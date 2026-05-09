package com.back.repositories;


import com.back.entities.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubTaskRepository extends JpaRepository<SubTask,Long> {


    List<SubTask> findAllByTaskId(Long taskId);

}
