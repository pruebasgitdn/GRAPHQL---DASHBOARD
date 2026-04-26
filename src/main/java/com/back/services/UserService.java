package com.back.services;

import com.back.entities.User;
import com.back.entities.dto.UserInput;
import com.back.entities.dto.UserResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    UserResponse registerUser(UserInput user);

    UserResponse findById(UUID id);

    List<UserResponse> findAll();

    List<UserResponse> findAllById(List<UUID> usersId);

}
