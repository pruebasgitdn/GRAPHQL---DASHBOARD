//package com.back.controllers;
//
//
//import com.back.entities.dto.AuthResponse;
//import com.back.entities.dto.LoginInput;
//import com.back.services.WspaceInvitationService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.graphql.data.method.annotation.Argument;
//import org.springframework.graphql.data.method.annotation.MutationMapping;
//import org.springframework.stereotype.Controller;
//
//import java.util.UUID;
//
//@Controller
//@RequiredArgsConstructor
//public class GraphQLInvitationController {
//
//    private final WspaceInvitationService wspaceInvitationService;
//
//    @MutationMapping(name = "sendInvitation")
//    public String sendInvitation(
//            @Argument UUID workspaceId,
//            @Argument String email
//
//    ) {
//        return wspaceInvitationService.inviteUserToWspace(workspaceId,email);
//    }
//
//    @MutationMapping(name = "confirmInvitation")
//    public String confirmInvitation(
//            @Argument String token
//    ) {
//        return wspaceInvitationService.confirmInvitation(token);
//    }
//
//}
