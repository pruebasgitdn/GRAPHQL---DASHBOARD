package com.back.entities.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskAssigneeInput {

    @NotNull(message = "La tarea debe ir asociado a un proyecto")
    private long taskId;

    @NotNull(message = "La tarea debe ir asociado a un proyecto")
    private UUID userId;

    @NotNull(message = "Ingresalo, para saber si es miembro de ese espacio")
    private UUID workspaceId;




}
