package com.pm.activityservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import com.pm.activityservice.repository.ActivityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import users.events.UserEvent;

import java.util.UUID;
@Service
public class KafkaConsumer {

    private final ActivityRepository activityRepository;
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    public KafkaConsumer(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @KafkaListener(topics = "user", groupId = "activity-service")
    public void consumeUserEvent(byte[] event) {
        try {
            UserEvent userEvent = UserEvent.parseFrom(event);

            log.info("Received User Event: [UserId={}, EventType={}]",
                    userEvent.getUserId(),
                    userEvent.getEventType());

            if ("USER_DELETED".equals(userEvent.getEventType())) {
                UUID userId = UUID.fromString(userEvent.getUserId());
                activityRepository.deleteByUserId(userId);
                log.info("Deleted activities for UserId={}", userId);
            }

        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing UserEvent: {}", e.getMessage());
        }
    }
}
