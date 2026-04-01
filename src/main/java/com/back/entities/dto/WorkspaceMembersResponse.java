package com.back.entities.dto;


import com.back.entities.User;
import com.back.entities.Workspace;
import com.back.enums.Role;
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
public class WorkspaceMembersResponse {


    private User user;

    private Workspace workspace;

    private Role role;

}
