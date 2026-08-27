package com.back.entities.dto;


import com.back.enums.TaskPriority;
import com.back.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class TaskTrendCL {

    private LocalDateTime day;
    private TaskPriority priority;
    private Long total;

    public TaskTrendCL(){}

    public TaskTrendCL(LocalDateTime day, TaskPriority priority, Long total) {
        this.day = day;
        this.priority = priority;
        this.total = total;
    }
}
