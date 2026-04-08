package com.back.entities.mappers;
import com.back.entities.TaskAssignee;
import com.back.entities.dto.TaskAssigneeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class TaskAssigneeMapper {
    private final UserMapper userMapper;
    private final TasksMapper taskMapper;



    public TaskAssigneeResponse toResponse(TaskAssignee entity) {
        return TaskAssigneeResponse.builder()
                .user(userMapper.toResponse(entity.getUser()))
                .task(taskMapper.toResponse(entity.getTask()))
                .build();
    }



}
