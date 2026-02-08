package com.pm.aiagent.service;

import activity.events.ActivityEvent;
import nutrition.events.NutritionEvent;
import org.junit.jupiter.api.Test;
import users.events.UserEvent;

import static org.assertj.core.api.Assertions.assertThat;

class PromptBuilderTest {

    private final PromptBuilder builder = new PromptBuilder();

    @Test
    void buildActivityPromptIncludesUserAndActivityDetails() {
        UserEvent user = UserEvent.newBuilder()
                .setUserId("user-1")
                .setName("Alex")
                .setEmail("alex@example.com")
                .setEventType("USER_CREATED")
                .build();

        ActivityEvent activity = ActivityEvent.newBuilder()
                .setActivityId("act-1")
                .setUserId("user-1")
                .setActivityType("RUNNING")
                .setDurationMinutes(45)
                .setCaloriesBurned(500)
                .build();

        String prompt = builder.buildActivityPrompt(activity, user);

        assertThat(prompt).contains("Alex");
        assertThat(prompt).contains("user-1");
        assertThat(prompt).contains("RUNNING");
        assertThat(prompt).contains("45 minutes");
        assertThat(prompt).contains("500");
    }

    @Test
    void buildNutritionPromptIncludesUserAndMealDetails() {
        UserEvent user = UserEvent.newBuilder()
                .setUserId("user-2")
                .setName("Jamie")
                .setEmail("jamie@example.com")
                .setEventType("USER_CREATED")
                .build();

        NutritionEvent nutrition = NutritionEvent.newBuilder()
                .setId("meal-1")
                .setUserId("user-2")
                .setMealType("LUNCH")
                .setCalories(650)
                .setProtein(40)
                .setCarbs(70)
                .setFat(20)
                .build();

        String prompt = builder.buildNutritionPrompt(nutrition, user);

        assertThat(prompt).contains("Jamie");
        assertThat(prompt).contains("LUNCH");
        assertThat(prompt).contains("650");
        assertThat(prompt).contains("40g");
        assertThat(prompt).contains("70g");
        assertThat(prompt).contains("20g");
    }
}
