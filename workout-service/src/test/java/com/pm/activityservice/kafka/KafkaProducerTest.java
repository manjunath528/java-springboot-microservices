package com.pm.activityservice.kafka;

import activity.events.ActivityEvent;
import com.pm.activityservice.model.Activity;
import com.pm.activityservice.model.ActivityStatus;
import com.pm.activityservice.model.WorkoutType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {

    @Mock
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @Captor
    private ArgumentCaptor<byte[]> payloadCaptor;

    @Test
    void sendActivityCreatedEventPublishesPayload() throws Exception {
        KafkaProducer producer = new KafkaProducer(kafkaTemplate);
        Activity activity = sampleActivity();

        producer.sendActivityCreatedEvent(activity);

        verify(kafkaTemplate).send(org.mockito.ArgumentMatchers.eq("activity"), payloadCaptor.capture());
        ActivityEvent event = ActivityEvent.parseFrom(payloadCaptor.getValue());
        assertThat(event.getActivityId()).isEqualTo(activity.getId().toString());
        assertThat(event.getUserId()).isEqualTo(activity.getUserId().toString());
        assertThat(event.getActivityType()).isEqualTo(activity.getWorkoutType().name());
        assertThat(event.getDurationMinutes()).isEqualTo(activity.getDurationMinutes());
        assertThat(event.getCaloriesBurned()).isEqualTo(activity.getCaloriesBurned());
        assertThat(event.getEventType()).isEqualTo(ActivityEvent.EventType.ACTIVITY_CREATED);
    }

    @Test
    void sendActivityCreatedEventSwallowsKafkaErrors() {
        KafkaProducer producer = new KafkaProducer(kafkaTemplate);
        Activity activity = sampleActivity();

        doThrow(new RuntimeException("Kafka down"))
                .when(kafkaTemplate)
                .send(org.mockito.ArgumentMatchers.eq("activity"), org.mockito.ArgumentMatchers.any(byte[].class));

        producer.sendActivityCreatedEvent(activity);

        verify(kafkaTemplate).send(org.mockito.ArgumentMatchers.eq("activity"), org.mockito.ArgumentMatchers.any(byte[].class));
    }

    private static Activity sampleActivity() {
        Activity activity = new Activity();
        activity.setId(UUID.randomUUID());
        activity.setUserId(UUID.randomUUID());
        activity.setWorkoutType(WorkoutType.RUNNING);
        activity.setDurationMinutes(30);
        activity.setCaloriesBurned(250);
        activity.setActivityDate(LocalDate.now());
        activity.setStatus(ActivityStatus.COMPLETED);
        return activity;
    }
}
