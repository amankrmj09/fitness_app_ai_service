package org.fitness.ai_service.model.entity;

import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class Activity {
    private String id;
    private String userId;
    private String type;
    private Integer duration;
    private Integer caloriesBurned;
    private Instant startTime;
    private Map<String, Object> additionalMetrics;
    private Instant createdAt;
    private Instant updatedAt;
}
