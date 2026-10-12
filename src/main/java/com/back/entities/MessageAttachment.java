package com.back.entities;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "message_attachments")

public class MessageAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    private String fileName;
    private String storageKey; // S3 o Cloudinary
    private String contentType;
    private Long fileSize;

    private LocalDateTime uploadedAt;


}
