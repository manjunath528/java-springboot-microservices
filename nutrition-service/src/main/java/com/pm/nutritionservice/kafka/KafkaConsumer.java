package com.pm.nutritionservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import com.pm.nutritionservice.service.NutritionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import user.events.UserEvent;

@Component
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(
            KafkaConsumer.class);
    private final NutritionService nutritionService;

    public KafkaConsumer(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @KafkaListener(topics = "user", groupId = "nutrition-service")
    public void consumeUserEvent(byte[] event) throws InvalidProtocolBufferException {
        UserEvent userEvent = UserEvent.parseFrom(event);
        log.info("Received User Delete Event[User ID: {}, Event Type: {}]",userEvent.getUserId(),userEvent.getEventType());
        if ("USER_DELETED".equals(userEvent.getEventType())) {
            log.info("Deleting Nutrition Details of User Id->{}",userEvent.getUserId());
            try {
                nutritionService.deleteNutritionForUser(userEvent.getUserId());
                log.info("Nutrition Details deleted successfully for user ->{}",userEvent.getUserId());
            } catch (Exception e) {
                log.error("Error deleting nutrition details of user -> {}",userEvent.getUserId());
            }

        }
    }
}
