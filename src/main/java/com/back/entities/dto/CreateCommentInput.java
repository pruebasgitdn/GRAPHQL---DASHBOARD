package com.back.entities.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentInput {


    @NotNull(message = "Ingrese el id de la tarea")
    private Long taskId;
    //@Size(max = 60,message = "Máximo 60 caracteres")


    @NotNull(message = "Ingrese el contenido")
    @Size(max = 300,message = "Máximo 300 caracteres")
    private String content;



}
