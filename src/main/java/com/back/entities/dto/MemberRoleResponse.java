package com.back.entities.dto;
import com.back.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRoleResponse {

    private UserResponse user;

    private Role role;
}
