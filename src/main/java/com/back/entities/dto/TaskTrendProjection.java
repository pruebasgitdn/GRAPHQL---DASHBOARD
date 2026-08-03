package com.back.entities.dto;

import com.back.enums.TaskPriority;
import com.back.enums.TaskStatus;

import java.time.LocalDate;

public interface TaskTrendProjection {

    LocalDate getDay();

    TaskPriority getPriority(); // o TaskStatus, según cómo tengas el enum

    Long getTotal();
}
