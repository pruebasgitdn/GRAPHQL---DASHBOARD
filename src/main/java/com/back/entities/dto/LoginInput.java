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
public class LoginInput {

    @NotNull(message = "El email no puede ser nulo")
    @Email(message = "Ingresa un email valido")
    private String email;


    @NotNull(message = "La contraseña no puede ser nula")
    @Size(max = 10,message = "La contraseña debe tener máximo 10 caracteres" )
    private String password;
}
