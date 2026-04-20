package org.fitness.ai_service.config;

import org.fitness.ai_service.model.entity.Recommendation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;

import java.util.UUID;

@Configuration
@EnableMongoAuditing
public class MongoConfig {

    @Bean
    public BeforeConvertCallback<Recommendation> activityBeforeConvertCallback() {
        return (recommendation, _) -> {
            if (recommendation.getId() == null) {
                recommendation.setId(UUID.randomUUID().toString());
            }
            return recommendation;
        };
    }
}
