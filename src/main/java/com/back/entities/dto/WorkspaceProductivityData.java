package com.back.entities.dto;
import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class WorkspaceProductivityData {

    private String name;
    private Long total;
    private Long completed;

}
