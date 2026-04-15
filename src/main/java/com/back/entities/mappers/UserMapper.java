package com.back.entities.mappers;


import com.back.entities.Task;
import com.back.entities.User;
import com.back.entities.dto.TaskResponse;
import com.back.entities.dto.UserInput;
import com.back.entities.dto.UserMeResponse;
import com.back.entities.dto.UserResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {


    public User toEntity(UserInput input) {
        User user = new User();
        user.setName(input.getName());
        user.setPassword(input.getPassword());
        user.setEmail(input.getEmail());
        user.setProfilePic(input.getProfilePic());

        return user;
    }

    public User FromResponseToEntity(UserResponse input) {
        User user = new User();
        user.setName(input.getName());
        user.setId(input.getId());
        user.setEmail(input.getEmail());
        user.setProfilePic(input.getProfilePic());

        return user;
    }



    public UserResponse toResponse(User input) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(input.getId());
        userResponse.setName(input.getName());
        userResponse.setEmail(input.getEmail());
        userResponse.setProfilePic(input.getProfilePic());

        return userResponse;
    }

    public List<UserResponse> mapUsers(List<User> users) {
        return users.stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .profilePic(user.getProfilePic())
                        .build())
                .toList();
    }


}
