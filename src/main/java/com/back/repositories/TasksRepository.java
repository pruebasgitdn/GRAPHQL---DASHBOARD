package com.back.repositories;

import com.back.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface TasksRepository extends JpaRepository<Task,Long> {

    List<Task> findAllById(Long id);
}
