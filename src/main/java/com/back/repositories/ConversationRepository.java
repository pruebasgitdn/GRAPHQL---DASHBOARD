package com.back.repositories;


import com.back.entities.Conversation;
import com.back.entities.dto.ConversationDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;


public interface ConversationRepository extends JpaRepository<Conversation, UUID> {


//    select *
//    from conversations
//    c join
//    conversation_members
//    cm on
//    cm.conversation_id =
//    c.id join
//    users u
//    on u.id =
//    cm.user_id where
//    c.type ='DIRECT'
//    and cm.
//
//    user_id
//    in('8622aec2-0bd9-4af7-8d4f-a9ddd0be0f8c','b4bafdfc-fa7a-4cb9-87a6-e746f65efb3e');



    //mimso orden(atributos) en el dto para el query q aca en el repo
    @Query("""
    SELECT new com.back.entities.dto.ConversationDTO(
        c.id,
        c.type,
        c.name,
        w.id,
        p.id
    )
    FROM Conversation c
    LEFT JOIN c.workspace w
    LEFT JOIN c.project p
    JOIN ConversationMembers cm ON cm.conversation.id = c.id
    WHERE c.type = com.back.enums.ConversationType.DIRECT
      AND cm.user.id IN (:userOne, :userTwo)
    GROUP BY c.id, c.type, c.name, w.id, p.id
    HAVING COUNT(DISTINCT cm.user.id) = 2
    
""")
    Optional<ConversationDTO> findDirectConversationBetweenTwoUsers(
            @Param("userOne") UUID userOne,
            @Param("userTwo") UUID userTwo
    );



}
