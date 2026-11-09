package com.back.services;

import com.back.entities.dto.MessagesDTO;

import java.util.List;
import java.util.UUID;

public interface MessagesService  {

    List<MessagesDTO> getMessagesByConversationId(UUID conversationId, UUID auth);
}
