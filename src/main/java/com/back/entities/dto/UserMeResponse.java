package com.back.entities.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMeResponse {

    private UUID id;
    private String email;
    private String name;
    private String profilePic;
    private String refreshToken;
    private String token;

}