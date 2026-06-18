package com.back.entities.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMultipleAssignationsInput {


    @NotNull(message = "Llena el id de la tarea a asignar")
    private long taskId;

    @NotNull(message = "Llena los id de los usuarios a asignar")
    private List<UUID> userIds;

    @NotNull(message = "Ingresalo, para saber si es miembro de ese espacio")
    private UUID workspaceId;


}
