package com.pm.notificationservice.kafka;
import activity.events.ActivityEvent;
import com.pm.notificationservice.model.NotificationType;
import com.pm.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ActivityEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(ActivityEventConsumer.class);

    private final NotificationService notificationService;

    public ActivityEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "activity",
            groupId = "notification-service"
    )
    public void consume(byte[] event) {
        try {
            ActivityEvent activityEvent = ActivityEvent.parseFrom(event);

            log.info(
                    "Activity event received -> activityId->{}, userId->{}, eventType->{}",
                    activityEvent.getActivityId(),
                    activityEvent.getUserId(),
                    activityEvent.getEventType()
            );

            switch (activityEvent.getEventType()) {

                case ACTIVITY_CREATED -> notificationService.createNotification(
                        activityEvent.getUserId(),
                        NotificationType.ACTIVITY_CREATED,
                        "Workout Started 💪",
                        "You started a "
                                + activityEvent.getActivityType()
                                + " workout for "
                                + activityEvent.getDurationMinutes()
                                + " minutes"
                );

                case ACTIVITY_COMPLETED -> notificationService.createNotification(
                        activityEvent.getUserId(),
                        NotificationType.ACTIVITY_COMPLETED,
                        "Workout Completed 🎉",
                        "Great job! You burned "
                                + activityEvent.getCaloriesBurned()
                                + " calories"
                );

                default -> log.info(
                        "Ignoring activity event type -> {}",
                        activityEvent.getEventType()
                );
            }

        } catch (Exception e) {
            log.error("Failed to consume activity event", e);
        }
    }
}
