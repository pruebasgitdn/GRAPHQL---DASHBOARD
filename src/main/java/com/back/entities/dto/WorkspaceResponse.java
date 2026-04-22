package com.back.entities.dto;


import com.back.entities.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceResponse {

    private UUID id;
    private String name;
    private UUID ownerId;
    private Long memberCount;
    private Long projectCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String color;




}
