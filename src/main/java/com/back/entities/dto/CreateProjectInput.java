package com.back.entities.dto;


import com.back.entities.Task;
import com.back.entities.Workspace;
import com.back.enums.ProjectStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectInput {

    @NotNull(message = "El nombre del proyecto no puede ser nulo")
    @Size(max = 60,message = "Máximo 60 caracteres")
    private String name;

    private String description;

    @NotNull(message = "Ingresa fecha de inicio")
    private LocalDate startDate;

    @NotNull(message = "Ingresa fecha de entrega")
    private LocalDate dueDate;

    @NotNull(message = "Ingresa el estado del proyecto")
    private ProjectStatus status;

    @NotNull(message = "El proyecto debe ir asociado a un espacio de trabajo(workspace)")
    private UUID workspaceId;

    @NotNull(message = "El proyecto debe ir asociado a un creador")
    private UUID ownerId;

    private List<Long> taskIds;



}
