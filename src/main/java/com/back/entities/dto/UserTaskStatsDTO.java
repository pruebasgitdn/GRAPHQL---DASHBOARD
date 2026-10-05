package com.back.entities.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class UserTaskStatsDTO {

    private UUID id;
    private String name;
    private String email;


    private Long todo;
    private Long done;
    private Long blocked;
    private Long inProgress;

    //@NoArgsConstructor
    public  UserTaskStatsDTO(){
    }


    //@AllArgsConstructor
    public UserTaskStatsDTO(UUID id,
                            String email,
                            String name,
                            Long todo,
                            Long done,
                            Long blocked,
                            Long inProgress
    ){
        this.email = email;
        this.id = id;
        this.name = name;
        this.todo = todo;
        this.done = done;
        this.blocked = blocked;
        this.inProgress = inProgress;

    }


}
