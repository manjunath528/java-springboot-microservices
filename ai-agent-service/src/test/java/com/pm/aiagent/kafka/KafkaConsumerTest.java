package com.pm.aiagent.kafka;

import activity.events.ActivityEvent;
import com.pm.aiagent.model.AiRecommendation;
import com.pm.aiagent.model.UserContext;
import com.pm.aiagent.service.AiEngine;
import com.pm.aiagent.store.UserContextStore;
import nutrition.events.NutritionEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import users.events.UserEvent;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerTest {

    @Mock
    private UserContextStore userStore;

    @Mock
    private AiEngine aiEngine;

    @Mock
    private AiRecommendationProducer producer;

    @Captor
    private ArgumentCaptor<AiRecommendation> recommendationCaptor;

    @Test
    void consumeUserEventStoresUserContext() {
        KafkaConsumer consumer = new KafkaConsumer(userStore, aiEngine, producer);

        UserEvent userEvent = UserEvent.newBuilder()
                .setUserId("user-1")
                .setName("Alex")
                .setEmail("alex@example.com")
                .setEventType("USER_CREATED")
                .build();

        consumer.consumeUserEvent(userEvent.toByteArray());

        verify(userStore).put(new UserContext("user-1", "Alex", "alex@example.com"));
    }

    @Test
    void consumeActivityEventPublishesRecommendation() throws Exception {
        KafkaConsumer consumer = new KafkaConsumer(userStore, aiEngine, producer);

        ActivityEvent activityEvent = ActivityEvent.newBuilder()
                .setActivityId("act-1")
                .setUserId("user-1")
                .setActivityType("RUNNING")
                .setDurationMinutes(30)
                .setCaloriesBurned(250)
                .build();

        UserContext userContext = new UserContext("user-1", "Alex", "alex@example.com");
        when(userStore.get("user-1")).thenReturn(Optional.of(userContext));
        when(aiEngine.coachForActivity(any(), any())).thenReturn("Great job!");

        consumer.consumeActivityEvent(activityEvent.toByteArray());

        verify(producer).publish(recommendationCaptor.capture());
        AiRecommendation recommendation = recommendationCaptor.getValue();
        assertThat(recommendation.userId()).isEqualTo("user-1");
        assertThat(recommendation.sourceType()).isEqualTo("ACTIVITY");
        assertThat(recommendation.message()).isEqualTo("Great job!");
        assertThat(recommendation.severity()).isEqualTo("INFO");
        assertThat(recommendation.createdAt()).isNotNull();
    }

    @Test
    void consumeNutritionEventPublishesWarningForHighCalories() throws Exception {
        KafkaConsumer consumer = new KafkaConsumer(userStore, aiEngine, producer);

        NutritionEvent nutritionEvent = NutritionEvent.newBuilder()
                .setId("meal-1")
                .setUserId("user-2")
                .setMealType("DINNER")
                .setCalories(900)
                .setProtein(45)
                .setCarbs(80)
                .setFat(30)
                .build();

        UserContext userContext = new UserContext("user-2", "Jamie", "jamie@example.com");
        when(userStore.get("user-2")).thenReturn(Optional.of(userContext));
        when(aiEngine.coachForNutrition(any(), any())).thenReturn("Consider lighter options.");

        consumer.consumeNutritionEvent(nutritionEvent.toByteArray());

        verify(producer).publish(recommendationCaptor.capture());
        AiRecommendation recommendation = recommendationCaptor.getValue();
        assertThat(recommendation.userId()).isEqualTo("user-2");
        assertThat(recommendation.sourceType()).isEqualTo("NUTRITION");
        assertThat(recommendation.message()).isEqualTo("Consider lighter options.");
        assertThat(recommendation.severity()).isEqualTo("WARNING");
        assertThat(recommendation.createdAt()).isNotNull();
    }
}
