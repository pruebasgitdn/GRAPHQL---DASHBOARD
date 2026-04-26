package com.back.entities.dto;

import com.back.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkSpaceDetailResponse {

    private UUID id;
    private String name;
    private UserResponse owner;
    private Long memberCount;
    private List<MemberRoleResponse> members;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ProjectSimpleResponse> projects;
    private Long projectCount;

    private String color;

}
