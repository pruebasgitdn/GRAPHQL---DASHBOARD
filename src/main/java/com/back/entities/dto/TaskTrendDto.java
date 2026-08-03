package com.back.entities.dto;

import com.back.enums.TaskStatus;

import java.time.LocalDate;

public record TaskTrendDto(
        LocalDate day,
        TaskStatus status,
        Long total

) {
}
