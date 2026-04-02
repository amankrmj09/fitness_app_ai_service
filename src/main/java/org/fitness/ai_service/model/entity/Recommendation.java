package org.fitness.ai_service.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "recommendations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recommendation {

    @Id
    private String id;

    private String userId;
    private String activityId;
    private String recommendationText;


    private List<String> recommendedImprovements;
    private List<String> suggestions;
    private List<String> safety;

    @CreatedDate
    private Instant createdAt;
}
