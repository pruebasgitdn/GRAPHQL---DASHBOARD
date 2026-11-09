package com.back.entities.dto;

import com.back.enums.ConversationType;

import java.util.UUID;

public record MessagesDTO(

        UUID id,
        UUID senderId,
        String emailSender,
        UUID conversationId,
        ConversationType conversationType,
        String content
) {



}
