package com.back.entities.dto;
import com.back.enums.ConversationType;
import lombok.AllArgsConstructor;

import java.util.UUID;


//mimso orden(atributos) en el repo para el query q aca
public record ConversationDTO(
        UUID id,
        ConversationType type,
        String name,
        UUID workspaceId,
        Long projectId

) {

}
