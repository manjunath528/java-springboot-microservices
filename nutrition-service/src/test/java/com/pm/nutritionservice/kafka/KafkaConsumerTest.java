package com.pm.nutritionservice.kafka;

import com.pm.nutritionservice.service.NutritionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import user.events.UserEvent;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerTest {

    @Mock
    private NutritionService nutritionService;

    @Test
    void consumeUserEventDeletesNutritionWhenUserDeleted() throws Exception {
        KafkaConsumer consumer = new KafkaConsumer(nutritionService);
        UserEvent event = UserEvent.newBuilder()
                .setUserId("user-1")
                .setName("Alex")
                .setEmail("alex@example.com")
                .setEventType("USER_DELETED")
                .build();

        consumer.consumeUserEvent(event.toByteArray());

        verify(nutritionService).deleteNutritionForUser("user-1");
    }

    @Test
    void consumeUserEventIgnoresNonDeleteEvents() throws Exception {
        KafkaConsumer consumer = new KafkaConsumer(nutritionService);
        UserEvent event = UserEvent.newBuilder()
                .setUserId("user-1")
                .setName("Alex")
                .setEmail("alex@example.com")
                .setEventType("USER_CREATED")
                .build();

        consumer.consumeUserEvent(event.toByteArray());

        verify(nutritionService, never()).deleteNutritionForUser("user-1");
    }

    @Test
    void consumeUserEventSwallowsDeleteErrors() throws Exception {
        KafkaConsumer consumer = new KafkaConsumer(nutritionService);
        UserEvent event = UserEvent.newBuilder()
                .setUserId("user-1")
                .setName("Alex")
                .setEmail("alex@example.com")
                .setEventType("USER_DELETED")
                .build();

        doThrow(new RuntimeException("db down"))
                .when(nutritionService)
                .deleteNutritionForUser("user-1");

        consumer.consumeUserEvent(event.toByteArray());

        verify(nutritionService).deleteNutritionForUser("user-1");
    }
}
