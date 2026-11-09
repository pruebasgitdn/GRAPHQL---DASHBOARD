package com.back.repositories;

import com.back.entities.ConversationMembers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConversationMembersRepository extends JpaRepository<ConversationMembers, UUID> {


    boolean existsByConversation_IdAndUser_Id(
            UUID conversationId,
            UUID userId
    );

}
