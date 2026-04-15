package com.back.entities.mappers;


import com.back.entities.Notification;
import com.back.entities.Project;
import com.back.entities.Task;
import com.back.entities.User;
import com.back.entities.dto.NotificationResponse;
import com.back.entities.dto.ProjectResponse;
import com.back.entities.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationMapper {

    private final UserMapper userMapper;
    private final TasksMapper tasksMapper;


    public Notification toEntity(NotificationResponse notificationResponse) {

        User userEnt = userMapper.FromResponseToEntity(notificationResponse.getUser());
        //Task taskEnt = userMapper.toEntity(notificationResponse.get());


        return Notification.builder()
                .id(notificationResponse.getId())
                .type(notificationResponse.getType())
                .message(notificationResponse.getMessage())
                .title(notificationResponse.getTitle())
                .user(userEnt)
                .build();
    }

    public NotificationResponse toResponse(Notification notification) {

        UserResponse userResp = userMapper.toResponse(notification.getUser());

       return  NotificationResponse.builder()
               .id(notification.getId())
               .message(notification.getMessage())
               .title(notification.getTitle())
               .type(notification.getType())
               .user(userResp)
               .build();
    }

}
