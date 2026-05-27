package com.back.controllers;


import com.back.entities.dto.WorkspaceResponse;
import com.back.services.WspaceInvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class WspaceInvitationController {

    private final WspaceInvitationService wspaceInvitationService;


    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "sendInvitation")
    public String sendInvitation(@Argument(name = "workspaceId") UUID workspaceId ,
                                 @Argument(name = "email") String email ){

        return  wspaceInvitationService.inviteUserToWspace(workspaceId,email);
    }


    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "sendMultipleInvitations")
    public String sendMultipleInvitations(@Argument(name = "workspaceId") UUID workspaceId ,
                                 @Argument(name = "emails") List<String> emails){

        return  wspaceInvitationService.sendMultipleInvitesToWspace(workspaceId,emails);
    }

    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "confirmInvitation")
    public String confirmInvitation(@Argument(name = "token") String token ){

        return  wspaceInvitationService.confirmInvitation(token);
    }

        //(UUID workspaceId,String email)

}
