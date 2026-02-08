package com.pm.notificationservice.kafka;

import activity.events.ActivityEvent;
import com.pm.notificationservice.model.NotificationType;
import com.pm.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ActivityEventConsumerTest {

    @Mock
    private NotificationService notificationService;

    @Test
    void consumeCreatesNotificationForActivityCreated() {
        ActivityEventConsumer consumer = new ActivityEventConsumer(notificationService);
        ActivityEvent event = ActivityEvent.newBuilder()
                .setActivityId("act-1")
                .setUserId("user-1")
                .setActivityType("RUNNING")
                .setDurationMinutes(30)
                .setEventType(ActivityEvent.EventType.ACTIVITY_CREATED)
                .build();

        consumer.consume(event.toByteArray());

        verify(notificationService).createNotification(
                "user-1",
                NotificationType.ACTIVITY_CREATED,
                "Workout Started 💪",
                "You started a RUNNING workout for 30 minutes"
        );
    }

    @Test
    void consumeCreatesNotificationForActivityCompleted() {
        ActivityEventConsumer consumer = new ActivityEventConsumer(notificationService);
        ActivityEvent event = ActivityEvent.newBuilder()
                .setActivityId("act-2")
                .setUserId("user-2")
                .setCaloriesBurned(500)
                .setEventType(ActivityEvent.EventType.ACTIVITY_COMPLETED)
                .build();

        consumer.consume(event.toByteArray());

        verify(notificationService).createNotification(
                "user-2",
                NotificationType.ACTIVITY_COMPLETED,
                "Workout Completed 🎉",
                "Great job! You burned 500 calories"
        );
    }

    @Test
    void consumeIgnoresOtherEventTypes() {
        ActivityEventConsumer consumer = new ActivityEventConsumer(notificationService);
        ActivityEvent event = ActivityEvent.newBuilder()
                .setActivityId("act-3")
                .setUserId("user-3")
                .setEventType(ActivityEvent.EventType.ACTIVITY_UPDATED)
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
