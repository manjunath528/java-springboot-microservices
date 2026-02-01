package com.pm.aiagent.service;

import activity.events.ActivityEvent;
import nutrition.events.NutritionEvent;
import com.pm.aiagent.model.UserContext;
import org.springframework.stereotype.Service;

@Service
public class AiEngine {

    private final LlmClient llmClient;

    public AiEngine(LlmClient llmClient) {
        this.llmClient = llmClient;
    }

    public String coachForActivity(ActivityEvent a, UserContext u) throws Exception {
        String prompt = """
        User: %s (%s)
        New activity event:
        - type: %s
        - durationMinutes: %d
        - caloriesBurned: %d

        Give one short actionable coaching message.
        """.formatted(u.name(), u.userId(), a.getActivityType(), a.getDurationMinutes(), a.getCaloriesBurned());

        return llmClient.generateCoachMessage(prompt);
    }

    public String coachForNutrition(NutritionEvent n, UserContext u) throws Exception {
        String prompt = """
        User: %s (%s)
        New nutrition event:
        - mealType: %s
        - calories: %d
        - protein: %dg, carbs: %dg, fat: %dg

        Give one short actionable nutrition coaching message.
        """.formatted(u.name(), u.userId(), n.getMealType(), n.getCalories(), n.getProtein(), n.getCarbs(), n.getFat());

        return llmClient.generateCoachMessage(prompt);
    }
}
