package com.back.controllers;


import com.back.entities.dto.MessagesDTO;
import com.back.security.UserDetailsImpl;
import com.back.services.MessagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class GraphQLMessagesController {


    private final MessagesService messagesService;

    @PreAuthorize("isAuthenticated()")
    @QueryMapping(name = "findMessagesByConversationId")
    public List<MessagesDTO> findMessagesByConversationId(
            @Argument(name = "conversationId") UUID conversationId,
            @AuthenticationPrincipal UserDetailsImpl authenticated
    ){
        if(authenticated == null){
            throw new AuthenticationCredentialsNotFoundException("No se econtraron las credenciales de autenticacion");
        }

        return messagesService.getMessagesByConversationId(conversationId,authenticated.getId());
    }


}
