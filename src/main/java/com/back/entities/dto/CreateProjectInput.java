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
public class CreateProjectInput {

    @NotNull(message = "La contraseña no puede ser nula")
    @Size(max = 40,message = "Máximo 40 caracteres")
    private String name;

    private String description;


}
