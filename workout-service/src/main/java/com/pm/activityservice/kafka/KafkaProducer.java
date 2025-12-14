package com.pm.activityservice.kafka;
import activity.events.ActivityEvent;
import com.pm.activityservice.model.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Service
public class KafkaProducer {

    private static final Logger log =
            LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final String activityTopic;

    public KafkaProducer(
            KafkaTemplate<String, byte[]> kafkaTemplate,
            @Value("${kafka.topics.activity}") String activityTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.activityTopic = activityTopic;
    }

    public void sendActivityCreatedEvent(Activity activity) {

        ActivityEvent event = ActivityEvent.newBuilder()
                .setActivityId(activity.getId().toString())
                .setUserId(activity.getUserId().toString())
                .setActivityType(activity.getWorkoutType().name())
                .setDurationMinutes(activity.getDurationMinutes())
                .setCaloriesBurned(activity.getCaloriesBurned())
                .setEventType(ActivityEvent.EventType.ACTIVITY_CREATED)
                .setTimestamp(System.currentTimeMillis())
                .build();

        kafkaTemplate.send(
                activityTopic,
                activity.getUserId().toString(),
                event.toByteArray()
        );

        log.info("Sent ACTIVITY_CREATED event to topic={}", activityTopic);
    }
}
