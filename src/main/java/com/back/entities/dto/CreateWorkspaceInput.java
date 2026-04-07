package com.back.entities.dto;
import com.back.entities.Project;
import com.back.entities.WorkspaceMember;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateWorkspaceInput {

    @NotNull(message = "La contraseña no puede ser nula")
    @Size(max = 40,message = "Máximo 40 caracteres" )
    private String name;


//
//    private List<WorkspaceMember> members = new ArrayList<>();
//
//    private List<Project> projects = new ArrayList<>();




}
