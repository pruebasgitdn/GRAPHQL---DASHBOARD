package com.back.entities.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskAssigneeResponse {

private  UserResponse user;
private  TaskResponse task;

}
