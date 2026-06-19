package com.back.entities.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TaskStats {
    private Long totalTasks;
    private Long todoTasks;
    private Long inProgressTasks;
    private Long doneTasks;

}
