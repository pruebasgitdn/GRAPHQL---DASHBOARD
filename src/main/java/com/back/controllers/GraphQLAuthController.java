package com.back.controllers;
import com.back.entities.User;
import com.back.entities.dto.AuthResponse;
import com.back.entities.dto.LoginInput;
import com.back.security.UserDetailsImpl;
import com.back.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class GraphQLAuthController {


    private final AuthenticationService authenticationService;
    //private final CookieService cookieService;


    @MutationMapping(name = "login")
    public AuthResponse login(@Valid @Argument LoginInput login) {
        return authenticationService.authenticate(
                login.getEmail(),
                login.getPassword()
        );
    }

    @QueryMapping(name = "me")
    public User me(@AuthenticationPrincipal UserDetailsImpl user) {
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getUsername())
                .build();
    }

}
