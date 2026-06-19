package com.back.entities.dto;

public interface TaskStatsProjection {
    Long getTotalTasks();
    Long getTodoTasks();
    Long getInProgressTasks();
    Long getDoneTasks();
}
