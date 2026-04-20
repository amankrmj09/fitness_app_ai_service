package org.fitness.ai_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fitness.ai_service.model.entity.Activity;
import org.fitness.ai_service.model.entity.Recommendation;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAiService {

    private final GeminiService geminiService;

    public Recommendation generateRecommendation(Activity activity) {
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getRecommendation(prompt);
        log.info("RESPONSE FROM AI: {}", aiResponse);
        return processAiResponse(activity, aiResponse);
    }

    private Recommendation processAiResponse(Activity activity, String aiResponse) {
        try {
            String jsonContent = aiResponse
                    .replaceAll("(?s)```json\\s*", "")
                    .replaceAll("(?s)```\\s*", "")
                    .trim();

            log.info("PARSED JSON: {}", jsonContent);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode analysisJson = mapper.readTree(jsonContent);
            JsonNode analysisNode = analysisJson.path("analysis");

            Map<String, String> analysisMap = new LinkedHashMap<>();
            if (!analysisNode.path("overall").isMissingNode())
                analysisMap.put("overall", analysisNode.path("overall").asText());
            if (!analysisNode.path("pace").isMissingNode())
                analysisMap.put("pace", analysisNode.path("pace").asText());
            if (!analysisNode.path("heartRate").isMissingNode())
                analysisMap.put("heartRate", analysisNode.path("heartRate").asText());
            if (!analysisNode.path("caloriesBurned").isMissingNode())
                analysisMap.put("calories", analysisNode.path("caloriesBurned").asText());

            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
            List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety = extractSafetyGuidelines(analysisJson.path("safety"));

            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(String.valueOf(activity.getType()))
                    .recommendation(analysisMap)
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safety(safety)
                    .createdAt(Instant.now())
                    .build();

        } catch (Exception e) {
            log.error("Failed to parse AI response: {}", e.getMessage());
            return createDefaultRecommendation(activity);
        }
    }

    private Recommendation createDefaultRecommendation(Activity activity) {
        Map<String, String> defaultAnalysis = new LinkedHashMap<>();
        defaultAnalysis.put("overall", "Unable to generate detailed analysis");

        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(String.valueOf(activity.getType()))
                .recommendation(defaultAnalysis)
                .improvements(Collections.singletonList("Continue with your current routine"))
                .suggestions(Collections.singletonList("Consider consulting a fitness professional"))
                .safety(Arrays.asList(
                        "Always warm up before exercise",
                        "Stay hydrated",
                        "Listen to your body"
                ))
                .createdAt(Instant.now())
                .build();
    }

    private List<String> extractSafetyGuidelines(JsonNode safetyNode) {
        List<String> safety = new ArrayList<>();
        if (safetyNode.isArray()) {
            safetyNode.forEach(item -> safety.add(item.asText()));
        }
        return safety.isEmpty()
                ? Collections.singletonList("Follow general safety guidelines")
                : safety;
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestions = new ArrayList<>();
        if (suggestionsNode.isArray()) {
            suggestionsNode.forEach(suggestion -> {
                String workout = suggestion.path("workout").asText();
                String description = suggestion.path("description").asText();
                suggestions.add(String.format("%s: %s", workout, description));
            });
        }
        return suggestions.isEmpty()
                ? Collections.singletonList("No specific suggestions provided")
                : suggestions;
    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> improvements = new ArrayList<>();
        if (improvementsNode.isArray()) {
            improvementsNode.forEach(improvement -> {
                String area = improvement.path("area").asText();
                String detail = improvement.path("recommendation").asText();
                improvements.add(String.format("%s: %s", area, detail));
            });
        }
        return improvements.isEmpty()
                ? Collections.singletonList("No specific improvements provided")
                : improvements;
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
                Analyze this fitness activity and provide recommendations in the following JSON format.
                
                IMPORTANT: Return ONLY raw JSON. No markdown, no ```json fences, no extra text before or after the JSON.
                
                Required JSON structure:
                {
                  "analysis": {
                    "overall": "Overall analysis here",
                    "pace": "Pace analysis here",
                    "heartRate": "Heart rate analysis here",
                    "caloriesBurned": "Calories analysis here"
                  },
                  "improvements": [
                    {
                      "area": "Area name",
                      "recommendation": "Detailed recommendation"
                    }
                  ],
                  "suggestions": [
                    {
                      "workout": "Workout name",
                      "description": "Detailed workout description"
                    }
                  ],
                  "safety": [
                    "Safety point 1",
                    "Safety point 2"
                  ]
                }
                
                Activity to analyze:
                - Activity Type: %s
                - Duration: %d minutes
                - Calories Burned: %d
                - Additional Metrics: %s
                
                Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
                """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
        );
    }
}
