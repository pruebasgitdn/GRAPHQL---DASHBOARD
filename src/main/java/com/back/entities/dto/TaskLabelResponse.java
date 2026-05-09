package com.back.entities.dto;

import com.back.enums.TaskLabelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskLabelResponse {

    private Long id;
    private Long task_Id;
    private TaskLabelType label;


}
