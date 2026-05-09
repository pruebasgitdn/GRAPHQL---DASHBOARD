package com.back.entities.mappers;


import com.back.entities.SubTask;
import com.back.entities.dto.SubTaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubTaskMapper {


    public SubTaskResponse toResponse (SubTask subTask){


        return SubTaskResponse.builder()
                .id(subTask.getId())
                .title(subTask.getTitle())
                .completed(subTask.getCompleted())
                .createdAt(subTask.getCreatedAt())
                .build();

    }

    public SubTask toEntity (SubTaskResponse subTaskResponse){

        return SubTask.builder()
                .id(subTaskResponse.getId())
                .title(subTaskResponse.getTitle())
                .completed(subTaskResponse.getCompleted())
                .createdAt(subTaskResponse.getCreatedAt())
                .build();

    }



}
