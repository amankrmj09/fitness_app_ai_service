# AI Service

A Spring Boot microservice for providing AI-driven recommendations and analysis in a fitness application ecosystem.

## Features
- Analyze fitness activity data
- Generate personalized recommendations
- RESTful API endpoints for analysis and suggestions

## Main Endpoints
- `POST /recommendations/analyze` — Analyze fitness activity and get recommendations
- `GET /recommendations` — Get all recommendations

## Main Classes
- `AiServiceApplication` — Main entry point
- `RecommendationController` — REST API endpoints
- `RecommendationService` — Business logic for recommendations
- `RecommendationRepository` — Data access for recommendations
- `Recommendation` — Model/entity for recommendations

## Configuration
- Main config: `src/main/resources/application.yaml`
- Profiles: `application-dev.yaml` for development
- Default port: 8080

## Build & Run
```sh
./gradlew build
./gradlew bootRun
```
