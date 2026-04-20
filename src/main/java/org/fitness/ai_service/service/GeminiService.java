package org.fitness.ai_service.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    private final ChatClient chatClient;
    private final ChatClient chatClientWithMemory;

    public GeminiService(ChatClient.Builder builder, ChatMemory chatMemory) {

        // without memory — stateless, each call is independent
        this.chatClient = builder.build();

        // with memory — remembers conversation history per conversationId
        this.chatClientWithMemory = builder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    // ─── Without Memory ───────────────────────────────────────────────

    public String getRecommendation(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }

    // ─── With Memory ──────────────────────────────────────────────────

    public String chatWithMemory(String conversationId, String userMessage) {
        return chatClientWithMemory.prompt()
                .user(userMessage)
                .advisors(a -> a.param(
                        ChatMemory.CONVERSATION_ID, conversationId
                ))
                .call()
                .content();
    }
}
