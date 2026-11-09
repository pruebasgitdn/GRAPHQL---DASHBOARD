package com.back.repositories;

import com.back.entities.Message;
import com.back.entities.dto.ConversationDTO;
import com.back.entities.dto.MessagesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessagesRepository extends JpaRepository<Message, UUID> {


    @Query("""
        SELECT new com.back.entities.dto.MessagesDTO(
           m.id,
                      m.sender.id,
                      m.sender.email,
                      m.conversation.id,
                      m.conversation.type,
                      m.content
        )
        FROM Message m
        WHERE m.conversation.id = :conversationId
        ORDER BY m.createdAt ASC
        """)
    List<MessagesDTO> findMessagesByConversationId(
            @Param("conversationId") UUID conversationId
    );





}
