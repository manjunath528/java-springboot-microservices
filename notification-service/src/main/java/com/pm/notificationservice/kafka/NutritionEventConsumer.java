package com.pm.notificationservice.kafka;
import com.pm.notificationservice.model.NotificationType;
import com.pm.notificationservice.service.NotificationService;
import nutrition.events.NutritionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NutritionEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(NutritionEventConsumer.class);

    private final NotificationService notificationService;

    public NutritionEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "nutrition",
            groupId = "notification-service"
    )
    public void consume(byte[] event) {
        try {
            NutritionEvent nutritionEvent = NutritionEvent.parseFrom(event);

            log.info(
                    "Nutrition event received -> nutritionId->{}, userId->{}, eventType->{}",
                    nutritionEvent.getId(),
                    nutritionEvent.getUserId(),
                    nutritionEvent.getEventType()
            );

            switch (nutritionEvent.getEventType()) {

                case NUTRITION_CREATED -> notificationService.createNotification(
                        nutritionEvent.getUserId(),
                        NotificationType.NUTRITION_CREATED,
                        "Nutrition Started",
                        "You started a "
                                + nutritionEvent.getId()
                                + " with "
                                + nutritionEvent.getMealType()
                                + " meal."
                );

                case NUTRITION_COMPLETED -> notificationService.createNotification(
                        nutritionEvent.getUserId(),
                        NotificationType.NUTRITION_COMPLETED,
                        "Nutrition Plan Completed 🎉",
                        "Great job! You completed taking "
                                + nutritionEvent.getProtein()
                                + " proteins regularly"
                );

                default -> log.info(
                        "Ignoring nutrition event type -> {}",
                        nutritionEvent.getEventType()
                );
            }

        } catch (Exception e) {
            log.error("Failed to consume nutrition event", e);
        }
    }
}
