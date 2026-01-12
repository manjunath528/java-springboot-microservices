package com.pm.aiagent.service;
import activity.events.ActivityEvent;
import nutrition.events.NutritionEvent;
import users.events.UserEvent;
import com.pm.aiagent.model.AiResponse;
import org.springframework.stereotype.Service;

@Service
public class AiEngine {

    public AiResponse processActivity(ActivityEvent activity, UserEvent user) {
        String message;
        String type = "WORKOUT";

        if (activity.getCaloriesBurned() < 100) {
            message = "Try increasing your workout intensity today!";
        } else {
            message = "Great job on your workout!";
        }

        return new AiResponse(user.getUserId(), message, type);
    }

    public AiResponse processNutrition(NutritionEvent nutrition, UserEvent user) {
        String message;
        String type = "NUTRITION";

        if (nutrition.getCalories() > 800) {
            message = "Consider a lighter meal for balanced nutrition.";
        } else {
            message = "Your meal is on track!";
        }

        return new AiResponse(user.getUserId(), message, type);
    }
}
