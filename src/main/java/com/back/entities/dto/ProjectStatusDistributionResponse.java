package com.back.entities.dto;


import com.back.enums.ProjectStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectStatusDistributionResponse {


    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private Long count;

}
