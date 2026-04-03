package org.fitness.ai_service.service;


import lombok.RequiredArgsConstructor;
import org.fitness.ai_service.exception.ResourceNotFoundException;
import org.fitness.ai_service.model.entity.Recommendation;
import org.fitness.ai_service.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;


    public Recommendation getActivityRecommendations(String activityId) {
        return recommendationRepository.findByActivityId(activityId).orElseThrow(
                () -> new ResourceNotFoundException("No recommendations found for activity: " + activityId)
        );
    }

    public List<Recommendation> getUserRecommendations(String userId) {
        return recommendationRepository.findByUserId(userId);
    }
}
