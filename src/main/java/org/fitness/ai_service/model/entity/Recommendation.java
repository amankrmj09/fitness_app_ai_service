package org.fitness.ai_service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Document(collection = "recommendations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndex(def = "{'userId': 1, 'activityId': -1}", name = "user_activityId_idx")
public class Recommendation {

    @Id
    private String id;

    private String activityId;
    private String userId;
    private String activityType;
    private Map<String, String> recommendation;
    private List<String> improvements;
    private List<String> suggestions;
    private List<String> safety;

    @CreatedDate
    private Instant createdAt;
}
