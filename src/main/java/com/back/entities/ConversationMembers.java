package com.back.entities;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;



//miembros del chat

//TODO: validar en el backend -> service q no se cree dos veces
//la misma relacion es decir la misma conversacion dos veces
@Entity
@Table(
        name = "conversation_members",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"conversation_id", "user_id"}
                )
        }
)
public class ConversationMembers {



    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime joinedAt;
    private LocalDateTime lastReadAt;
    //private LocalDateTime mutedUntil;
    private boolean active = true;


}
