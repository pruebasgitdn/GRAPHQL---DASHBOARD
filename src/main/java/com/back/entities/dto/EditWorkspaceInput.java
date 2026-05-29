package com.back.entities.dto;


import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditWorkspaceInput {

    @Size(min = 7,max = 7, message =  "El color debe tener 7 digitos hexa con #6")
    private String color;

    private String name;



}
