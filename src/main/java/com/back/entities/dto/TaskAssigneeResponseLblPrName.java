package com.back.entities.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskAssigneeResponseLblPrName {

    private  UserResponse user;
    private  TaskLabelProjectDto task;
}
