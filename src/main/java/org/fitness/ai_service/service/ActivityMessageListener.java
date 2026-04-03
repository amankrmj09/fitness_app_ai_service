package org.fitness.ai_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fitness.ai_service.model.entity.Activity;
import org.fitness.ai_service.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    private final ActivityAiService activityAiService;
    private final RecommendationRepository recommendationRepository;

    @Value("${kafka.topic.name}")
    private String topicName;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "ai-service-group")
    public void processActivity(Activity activity){
        log.info("Received activity from Kafka topic {}: {}", topicName, activity);

        var recommendation = activityAiService.generateRecommendation(activity);
        recommendationRepository.save(recommendation);

    }
}
