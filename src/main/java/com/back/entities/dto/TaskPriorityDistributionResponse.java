package com.back.entities.dto;
import com.back.enums.TaskPriority;
import com.back.enums.TaskStatus;
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
public class TaskPriorityDistributionResponse {

    @Enumerated(EnumType.STRING)
    private TaskStatus priority;

    private Long count;

}
