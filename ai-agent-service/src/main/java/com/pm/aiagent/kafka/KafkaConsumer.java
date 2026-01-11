package com.pm.aiagent.kafka;
import activity.events.ActivityEvent;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import users.events.UserEvent;
import nutrition.events.NutritionEvent;

@Service
public class KafkaConsumer {

  private static final Logger log = LoggerFactory.getLogger(
          KafkaConsumer.class);

  @KafkaListener(topics = "activity", groupId = "ai-agent-service")
  public void consumeActivityEvent(byte[] event) {
    try {
      ActivityEvent userEvent = ActivityEvent.parseFrom(event);
      // ... perform any business related to analytics here

      log.info("Received Activity Event: [ActivityId={},UserId={},Event Type={}, Activity Type={}]",
              userEvent.getActivityId(),
              userEvent.getUserId(),
              userEvent.getEventType(),
              userEvent.getActivityType());
    } catch (InvalidProtocolBufferException e) {
      log.error("Error Deserializing event {}", e.getMessage());
    }
  }

  @KafkaListener(topics = "user", groupId = "ai-agent-service")
  public void consumeUserEvent(byte[] event) {
    try {
      UserEvent userEvent = UserEvent.parseFrom(event);
      // ... perform any business related to agents here

      log.info("Received User Event: [UserId={},User Name={}, Event Type={}, User Email={}]",
              userEvent.getUserId(),
              userEvent.getName(),
              userEvent.getEventType(),
              userEvent.getEmail());
    } catch (InvalidProtocolBufferException e) {
      log.error("Error deserializing event {}", e.getMessage());
    }
  }

  @KafkaListener(topics = "nutrition", groupId = "ai-agent-service")
  public void consumeNutritionEvent(byte[] event) {
    try {
      NutritionEvent nutritionEvent = NutritionEvent.parseFrom(event);
      // ... perform any business related to agents here

      log.info("Received Nutrition Event: [Id={},UserId={}, Event Type={}, Calories={}]",
              nutritionEvent.getId(),
              nutritionEvent.getUserId(),
              nutritionEvent.getEventType(),
              nutritionEvent.getCalories());
    } catch (InvalidProtocolBufferException e) {
      log.error("Error deserializing event {}", e.getMessage());
    }
  }
}
