package com.pm.activityservice.kafka;

import com.pm.activityservice.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import users.events.UserEvent;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerTest {

    @Mock
    private ActivityService activityService;

    @Test
    void consumeUserEventDeletesActivitiesWhenUserDeleted() throws Exception {
        KafkaConsumer consumer = new KafkaConsumer(activityService);
        UserEvent event = UserEvent.newBuilder()
                .setUserId("user-1")
                .setName("Alex")
                .setEmail("alex@example.com")
                .setEventType("USER_DELETED")
                .build();

        consumer.consumeUserEvent(event.toByteArray());

        verify(activityService).deleteActivitiesForUser("user-1");
    }

    @Test
    void consumeUserEventIgnoresNonDeleteEvents() throws Exception {
        KafkaConsumer consumer = new KafkaConsumer(activityService);
        UserEvent event = UserEvent.newBuilder()
                .setUserId("user-1")
                .setName("Alex")
                .setEmail("alex@example.com")
                .setEventType("USER_CREATED")
                .build();

        consumer.consumeUserEvent(event.toByteArray());

        verify(activityService, never()).deleteActivitiesForUser("user-1");
    }

    @Test
    void consumeUserEventSwallowsDeleteErrors() throws Exception {
        KafkaConsumer consumer = new KafkaConsumer(activityService);
        UserEvent event = UserEvent.newBuilder()
                .setUserId("user-1")
                .setName("Alex")
                .setEmail("alex@example.com")
                .setEventType("USER_DELETED")
                .build();

        doThrow(new RuntimeException("db down"))
                .when(activityService)
                .deleteActivitiesForUser("user-1");

        consumer.consumeUserEvent(event.toByteArray());

        verify(activityService).deleteActivitiesForUser("user-1");
    }
}
