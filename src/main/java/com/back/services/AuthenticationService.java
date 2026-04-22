package com.back.services;

import com.back.entities.dto.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {


    AuthResponse authenticate(String email, String password);

    String generateToken(UserDetails userDetails);

    UserDetails validateToken(String token);

    String generateRefreshToken(UserDetails userDetails);

    boolean isAccessToken(String token);

    boolean isRefreshToken(String token);

    AuthResponse refreshToken(String token);

}
