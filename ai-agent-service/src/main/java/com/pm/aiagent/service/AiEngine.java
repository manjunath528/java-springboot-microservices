package com.pm.aiagent.service;

import activity.events.ActivityEvent;
import nutrition.events.NutritionEvent;
import users.events.UserEvent;
import com.pm.aiagent.model.AiResponse;
import org.springframework.stereotype.Service;

@Service
public class AiEngine {

    private final PromptBuilder promptBuilder;
    private final LlmClient llmClient;

    public AiEngine(PromptBuilder promptBuilder, LlmClient llmClient) {
        this.promptBuilder = promptBuilder;
        this.llmClient = llmClient;
    }

    public AiResponse processActivity(ActivityEvent activity, UserEvent user) {
        try {
            String prompt = promptBuilder.buildActivityPrompt(activity, user);
            String aiText = llmClient.generateResponse(prompt);

            return new AiResponse(
                    user.getUserId(),
                    aiText,
                    "WORKOUT_AI"
            );
        } catch (Exception e) {
            return new AiResponse(
                    user.getUserId(),
                    "Keep going! Consistency is key.",
                    "FALLBACK"
            );
        }
    }

    public AiResponse processNutrition(NutritionEvent nutrition, UserEvent user) {
        try {
            String prompt = promptBuilder.buildNutritionPrompt(nutrition, user);
            String aiText = llmClient.generateResponse(prompt);

            return new AiResponse(
                    user.getUserId(),
                    aiText,
                    "NUTRITION_AI"
            );
        } catch (Exception e) {
            return new AiResponse(
                    user.getUserId(),
                    "Try maintaining a balanced diet today.",
                    "FALLBACK"
            );
        }
    }
}
