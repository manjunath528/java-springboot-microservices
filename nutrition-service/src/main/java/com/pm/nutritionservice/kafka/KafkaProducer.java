package com.pm.nutritionservice.kafka;

import com.pm.nutritionservice.model.Nutrition;
import nutrition.events.NutritionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(
            KafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNutritionCreatedEvent(Nutrition nutrition) {

        NutritionEvent event = NutritionEvent.newBuilder()
                .setId(nutrition.getId().toString())
                .setUserId(nutrition.getUserId().toString())
                .setCalories(nutrition.getCalories())
                .setCarbs(nutrition.getCarbs())
                .setFat(nutrition.getFat())
                .setProtein(nutrition.getProtein())
                .setEventType(String.valueOf(NutritionEvent.EventType.NUTRITION_CREATED))
                .setDate(System.currentTimeMillis())
                .build();

        try {
            kafkaTemplate.send("nutrition", event.toByteArray());
        } catch (Exception e) {
            log.error("Error sending NutritionCreated event: {}", event);
        }
    }
}
