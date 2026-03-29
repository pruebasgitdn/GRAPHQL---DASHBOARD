package com.back.entities.mappers;


import com.back.entities.User;
import com.back.entities.dto.UserInput;
import org.springframework.stereotype.Component;

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

}
