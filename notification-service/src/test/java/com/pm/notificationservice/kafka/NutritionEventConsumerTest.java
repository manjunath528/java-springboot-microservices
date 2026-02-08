package com.pm.notificationservice.kafka;

import com.pm.notificationservice.model.NotificationType;
import com.pm.notificationservice.service.NotificationService;
import nutrition.events.NutritionEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NutritionEventConsumerTest {

    @Mock
    private NotificationService notificationService;

    @Test
    void consumeCreatesNotificationForNutritionCreated() {
        NutritionEventConsumer consumer = new NutritionEventConsumer(notificationService);
        NutritionEvent event = NutritionEvent.newBuilder()
                .setId("meal-1")
                .setUserId("user-1")
                .setMealType("LUNCH")
                .setEventType(NutritionEvent.EventType.NUTRITION_CREATED)
                .build();

        consumer.consume(event.toByteArray());

        verify(notificationService).createNotification(
                "user-1",
                NotificationType.NUTRITION_CREATED,
                "Nutrition Started",
                "You started a meal-1 with LUNCH meal."
        );
    }

    @Test
    void consumeCreatesNotificationForNutritionCompleted() {
        NutritionEventConsumer consumer = new NutritionEventConsumer(notificationService);
        NutritionEvent event = NutritionEvent.newBuilder()
                .setId("meal-2")
                .setUserId("user-2")
                .setProtein(120)
                .setEventType(NutritionEvent.EventType.NUTRITION_COMPLETED)
                .build();

        consumer.consume(event.toByteArray());

        verify(notificationService).createNotification(
                "user-2",
                NotificationType.NUTRITION_COMPLETED,
                "Nutrition Plan Completed 🎉",
                "Great job! You completed taking 120 proteins regularly"
        );
    }

    @Test
    void consumeIgnoresOtherEventTypes() {
        NutritionEventConsumer consumer = new NutritionEventConsumer(notificationService);
        NutritionEvent event = NutritionEvent.newBuilder()
                .setId("meal-3")
                .setUserId("user-3")
                .setEventType(NutritionEvent.EventType.NUTRITION_UPDATED)
                .build();

        consumer.consume(event.toByteArray());

        verify(notificationService, never()).createNotification(
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any()
        );
    }
}
