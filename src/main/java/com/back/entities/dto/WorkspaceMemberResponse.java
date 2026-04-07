package com.back.entities.dto;


import com.back.entities.User;
import com.back.entities.Workspace;
import com.back.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceMemberResponse {

    private Long id;

    private UserResponse user;

    private WorkspaceResponse workspace;

    private Role role;



}
