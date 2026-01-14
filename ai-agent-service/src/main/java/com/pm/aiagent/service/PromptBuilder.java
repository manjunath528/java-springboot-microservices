package com.pm.aiagent.service;

import activity.events.ActivityEvent;
import nutrition.events.NutritionEvent;
import org.springframework.stereotype.Service;
import users.events.UserEvent;
import org.springframework.stereotype.Component;

@Service
public class PromptBuilder {

    public String buildActivityPrompt(ActivityEvent activity, UserEvent user) {
        return """
        User Profile:
        - Name: %s
        - UserId: %s

        Workout Details:
        - Activity: %s
        - Duration: %d minutes
        - Calories Burned: %d

        Task:
        Provide personalized workout feedback and a motivational suggestion.
        """
                .formatted(
                        user.getName(),
                        user.getUserId(),
                        activity.getActivityType(),
                        activity.getDurationMinutes(),
                        activity.getCaloriesBurned()
                );
    }

    public String buildNutritionPrompt(NutritionEvent nutrition, UserEvent user) {
        return """
        User Profile:
        - Name: %s

        Meal Details:
        - Meal Type: %s
        - Calories: %d
        - Protein: %dg
        - Carbs: %dg
        - Fat: %dg

        Task:
        Give nutrition advice and improvement suggestions.
        """
                .formatted(
                        user.getName(),
                        nutrition.getMealType(),
                        nutrition.getCalories(),
                        nutrition.getProtein(),
                        nutrition.getCarbs(),
                        nutrition.getFat()
                );
    }
}
