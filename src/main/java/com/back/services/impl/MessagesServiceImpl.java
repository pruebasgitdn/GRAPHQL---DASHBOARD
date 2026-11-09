package com.back.services.impl;

import com.back.entities.dto.MessagesDTO;
import com.back.repositories.ConversationMembersRepository;
import com.back.repositories.MessagesRepository;
import com.back.services.MessagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MessagesServiceImpl implements MessagesService {

    private final ConversationMembersRepository conversationMembersRepository;
    private final MessagesRepository messagesRepository;


    @Override
    public List<MessagesDTO> getMessagesByConversationId(UUID conversationId, UUID auth) {

        if(!conversationMembersRepository.existsByConversation_IdAndUser_Id(conversationId, auth)) {
            throw new RuntimeException("No eres miembro de esta conversacion no tienes permisos");
        }


        return messagesRepository.findMessagesByConversationId(conversationId);
    }
}
