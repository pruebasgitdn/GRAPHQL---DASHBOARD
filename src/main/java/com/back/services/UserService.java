package com.back.services;

import com.back.entities.User;
import com.back.entities.dto.UserInput;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User registerUser(UserInput user);


    Optional<User> findById(UUID id);

    List<User> findAll();


}
