package com.back.controllers;
import com.back.entities.User;
import com.back.entities.dto.UserInput;
import com.back.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class GrapQLUserController {

    private final UserService userService;

    // @PreAuthorize("hasRole('USER')") => revisar cual meti verdaderamente


    @MutationMapping(name = "registerStudent")
    public User registerUser(@Valid @Argument UserInput user){
        return userService.registerUser(user);
    }

    @QueryMapping(name = "userList")
    public List<User> userList(){
        return userService.findAll();
    }


    @QueryMapping(name = "user")
    public Optional<User> user(@Argument(name =  "id") UUID id){
        return userService.findById(id);
    }





}
