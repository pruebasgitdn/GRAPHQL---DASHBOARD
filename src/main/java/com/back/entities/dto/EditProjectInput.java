package com.back.entities.dto;

import com.back.enums.ProjectStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class EditProjectInput {

    @Size(max = 60,message = "Máximo 60 caracteres")
    private String name;

    @Size(max = 200,message = "Máximo 200 caracteres")
    private String description;

    private LocalDate startDate;

    private LocalDate dueDate;

    private ProjectStatus status;

    private List<Long> taskIds;

}
