package com.back.entities.dto;

import com.back.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSimpleResponse {

    private Long id;
    private String name;
    private String description;
    private Long tasksCount;
    private LocalDateTime createdAt;
    private LocalDate startDate;
    private ProjectStatus status;
}
