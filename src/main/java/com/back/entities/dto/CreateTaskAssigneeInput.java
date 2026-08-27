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

    @NotNull(message = "Llena el id de la tarea a asignar")
    private long taskId;

    @NotNull(message = "Llena el id del usuario a asignar")
    private UUID userId;

//    @NotNull(message = "Ingresalo, para saber si es miembro de ese espacio")
//    private UUID workspaceId;




}
