package com.back.services;

import com.back.entities.Conversation;
import com.back.entities.dto.ConversationDTO;

import java.util.Optional;
import java.util.UUID;

public interface ConversationServices {

    ConversationDTO findDirectConversationBetweenTwoUsersService (UUID userOne, UUID userTwo);

}
