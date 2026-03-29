package com.back.services;

import com.back.entities.dto.AuthResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {


    AuthResponse authenticate(String email, String password);

    String generateToken(UserDetails userDetails);

    UserDetails validateToken(String token);


}
