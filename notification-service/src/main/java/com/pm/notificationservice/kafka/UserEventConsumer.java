package com.pm.notificationservice.kafka;

import com.pm.notificationservice.model.NotificationType;
import com.pm.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import users.events.UserEvent;

@Service
public class UserEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(UserEventConsumer.class);
    private final NotificationService notificationService;

    public UserEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "user", groupId = "notification-service")
    public void consume(byte[] event) {
        try {
            UserEvent userEvent = UserEvent.parseFrom(event);

            log.info("User event received -> userId->{}, eventType->{}",
                    userEvent.getUserId(),
                    userEvent.getEventType());

            if ("USER_CREATED".equals(userEvent.getEventType())) {
                notificationService.createNotification(
                        userEvent.getUserId(),
                        NotificationType.USER_CREATED,
                        "Welcome!",
                        "Welcome to the platform, " + userEvent.getName()
                );
            }

        } catch (Exception e) {
            log.error("Failed to consume user event", e);
        }
    }
}
