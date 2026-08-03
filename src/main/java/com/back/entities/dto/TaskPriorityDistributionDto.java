package com.back.entities.dto;

import com.back.enums.TaskPriority;

public record TaskPriorityDistributionDto(
        TaskPriority priority,
        Long total

) {
}
