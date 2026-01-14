package com.pm.aiagent.service;
import activity.events.ActivityEvent;
import nutrition.events.NutritionEvent;
import users.events.UserEvent;
import org.springframework.stereotype.Service;

@Service
public class AiDecisionService {

    public boolean shouldInvokeAiForActivity(ActivityEvent activity) {
        return activity.getDurationMinutes() > 0;
    }

    public boolean isLowEffortWorkout(ActivityEvent activity) {
        return activity.getCaloriesBurned() < 100;
    }

    public boolean isHighCalorieMeal(NutritionEvent nutrition) {
        return nutrition.getCalories() > 800;
    }

    public String selectAgentPersona(UserEvent user) {
        return "FITNESS_COACH";
    }
}
