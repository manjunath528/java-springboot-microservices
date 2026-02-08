package com.pm.activityservice.kafka;

import activity.events.ActivityEvent;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pm.activityservice.repository.ActivityRepository;
import com.pm.activityservice.service.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import users.events.UserEvent;
@Component
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(
            KafkaConsumer.class);
    private final ActivityService activityService;

    public KafkaConsumer(ActivityService activityService) {
        this.activityService = activityService;
    }

    @KafkaListener(topics = "user", groupId = "activity-service")
    public void consumeUserEvent(byte[] event) throws InvalidProtocolBufferException {
        UserEvent userEvent = UserEvent.parseFrom(event);
        log.info("Received User Event[User ID: {}, Event Type: {}]", userEvent.getUserId(), userEvent.getEventType());
        if ("USER_DELETED".equals(userEvent.getEventType())) {
            log.info("Deleting Activity Details of User Id->{}", userEvent.getUserId());
            try {
                activityService.deleteActivitiesForUser(userEvent.getUserId());
                log.info("Activity Details deleted successfully for user ->{}", userEvent.getUserId());
            } catch (Exception e) {
                log.error("Error deleting activity details of user -> {}", userEvent.getUserId(), e);
            }
        }
    }
}
