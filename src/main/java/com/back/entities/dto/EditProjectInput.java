package com.back.entities.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class EditProjectInput {

    private Long id;
    //private UUID workspaceId;
    private String name;
    private String description;
    private List<Long> taskIds;

}
