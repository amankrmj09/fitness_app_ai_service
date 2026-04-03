package org.fitness.ai_service.model.dto;

import lombok.Data;

@Data
public class ChatRequestDTO {
    private String conversationId;
    private String message;
}
