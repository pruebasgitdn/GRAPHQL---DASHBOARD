package com.back.repositories;

import com.back.entities.TaskLabel;
import com.back.enums.TaskLabelType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskLabelRepository extends JpaRepository<TaskLabel,Long> {

    boolean existsByTask_IdAndLabel(Long task_id, TaskLabelType labelType);

}
