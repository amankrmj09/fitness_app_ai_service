package org.fitness.ai_service.controller;

import lombok.RequiredArgsConstructor;
import org.fitness.ai_service.model.dto.ChatRequestDTO;
import org.fitness.ai_service.model.entity.Recommendation;
import org.fitness.ai_service.service.GeminiService;
import org.fitness.ai_service.service.RecommendationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final GeminiService geminiService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getUserRecommendations(@PathVariable String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                recommendationService.getUserRecommendations(userId)
        );
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Recommendation> getActivityRecommendations(@PathVariable String activityId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                recommendationService.getActivityRecommendations(activityId)
        );
    }

    // ─── Chat with Memory ─────────────────────────────────────────────
    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody ChatRequestDTO request) {
        String response = geminiService.chatWithMemory(
                request.getConversationId(),
                request.getMessage()
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
