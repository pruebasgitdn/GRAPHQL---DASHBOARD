package com.back.controllers;


import com.back.entities.Conversation;
import com.back.entities.dto.ConversationDTO;
import com.back.services.ConversationServices;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class GrapQLControllerConversation {

    private final ConversationServices conversationServices;


    @QueryMapping(name = "findDirectConversationBetweenTwoUsers")
    public ConversationDTO findDirectConversation(@Argument(name = "userOne") UUID userOne,
                                                  @Argument(name = "userTwo") UUID userTwo
                                               ){
      return  conversationServices.findDirectConversationBetweenTwoUsersService(userOne,userTwo);
    };

}
