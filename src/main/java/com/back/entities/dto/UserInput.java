package com.back.entities.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInput {

    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 3,max = 30,message = "El nombre debe tener minimo 3 y máximo 30 caracteres" )
    private String name;

    @NotNull(message = "El email no puede ser nulo")
    @Email(message = "Ingresa un email valido")
    private String email;

    @NotNull(message = "La contraseña no puede ser nula")
    @Size(min = 5,max = 10,message = "La contraseña debe tener minimo 3 y máximo 10 caracteres" )
    private String password;

    private String profilePic;


}
