package com.back.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubTaskResponse {

    private Long id;

    private String title;

    private Boolean completed;

    private LocalDateTime createdAt;

}

