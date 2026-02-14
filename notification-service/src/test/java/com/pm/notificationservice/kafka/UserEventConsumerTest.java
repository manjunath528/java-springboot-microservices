package com.pm.notificationservice.kafka;

import com.pm.notificationservice.model.NotificationType;
import com.pm.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import users.events.UserEvent;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserEventConsumerTest {

    @Mock
    private NotificationService notificationService;

    @Test
    void consumeCreatesNotificationForUserCreated() {
        UserEventConsumer consumer = new UserEventConsumer(notificationService);
        UserEvent event = UserEvent.newBuilder()
                .setUserId("user-1")
                .setName("Alex")
                .setEmail("alex@example.com")
                .setEventType("USER_CREATED")
                .build();

        consumer.consume(event.toByteArray());

        verify(notificationService).createNotification(
                "user-1",
                NotificationType.USER_CREATED,
                "Welcome!",
                "Welcome to the platform, Alex"
        );
    }

    @Test
    void consumeIgnoresOtherEventTypes() {
        UserEventConsumer consumer = new UserEventConsumer(notificationService);
        UserEvent event = UserEvent.newBuilder()
                .setUserId("user-2")
                .setName("Jamie")
                .setEmail("jamie@example.com")
                .setEventType("USER_DELETED")
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
