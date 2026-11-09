package com.back.services.impl;


import com.back.entities.Conversation;
import com.back.entities.dto.ConversationDTO;
import com.back.exceptions.ItemNotFoundException;
import com.back.repositories.ConversationRepository;
import com.back.services.ConversationServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationServicesImpl implements ConversationServices {


    private final ConversationRepository conversationRepository;


    @Override
    public ConversationDTO findDirectConversationBetweenTwoUsersService(UUID userOne, UUID userTwo) {



      return  conversationRepository
                .findDirectConversationBetweenTwoUsers(userOne, userTwo)
                .orElseThrow(() ->
                        new ItemNotFoundException(
                                "No se encontró la conversación directa"
                        )
                );

    }
}
