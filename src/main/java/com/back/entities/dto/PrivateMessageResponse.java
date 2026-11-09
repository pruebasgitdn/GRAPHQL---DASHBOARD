package com.back.entities.dto;

public record PrivateMessageResponse(
        String senderUsername,
        String content
) {
}
