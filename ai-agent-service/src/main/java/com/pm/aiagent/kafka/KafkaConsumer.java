package com.pm.aiagent.kafka;

import activity.events.ActivityEvent;
import com.google.protobuf.InvalidProtocolBufferException;
import nutrition.events.NutritionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import users.events.UserEvent;

@Service
public class KafkaConsumer {

  private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

  /**
   * Consume activity events from Kafka and process them.
   */
  @KafkaListener(topics = "activity", groupId = "ai-agent-service")
  public void consumeActivityEvent(byte[] event) {
    try {
      ActivityEvent activityEvent = ActivityEvent.parseFrom(event);

      log.info("Received Activity Event: [ActivityId={}, UserId={}, EventType={}, ActivityType={}, Duration={}, Calories={}]",
              activityEvent.getActivityId(),
              activityEvent.getUserId(),
              activityEvent.getEventType(),
              activityEvent.getActivityType(),
              activityEvent.getDurationMinutes(),
              activityEvent.getCaloriesBurned());

      // Build a dummy UserEvent for demonstration
      UserEvent dummyUser = UserEvent.newBuilder()
              .setUserId(activityEvent.getUserId())
              .setName("Unknown")
              .setEmail("unknown@example.com")
              .setEventType("ACTIVITY_EVENT")
              .build();

      // Call your AI processing method (placeholder)
      processActivityEvent(activityEvent, dummyUser);

    } catch (InvalidProtocolBufferException e) {
      log.error("Error deserializing ActivityEvent: {}", e.getMessage());
    }
  }

  /**
   * Consume user events from Kafka and process them.
   */
  @KafkaListener(topics = "user", groupId = "ai-agent-service")
  public void consumeUserEvent(byte[] event) {
    try {
      UserEvent userEvent = UserEvent.parseFrom(event);

      log.info("Received User Event: [UserId={}, Name={}, Email={}, EventType={}]",
              userEvent.getUserId(),
              userEvent.getName(),
              userEvent.getEmail(),
              userEvent.getEventType());

      // Call AI processing for user events
      processUserEvent(userEvent);

    } catch (InvalidProtocolBufferException e) {
      log.error("Error deserializing UserEvent: {}", e.getMessage());
    }
  }

  /**
   * Consume nutrition events from Kafka and process them.
   */
  @KafkaListener(topics = "nutrition", groupId = "ai-agent-service")
  public void consumeNutritionEvent(byte[] event) {
    try {
      NutritionEvent nutritionEvent = NutritionEvent.parseFrom(event);

      log.info("Received Nutrition Event: [Id={}, UserId={}, MealType={}, Calories={}, EventType={}]",
              nutritionEvent.getId(),
              nutritionEvent.getUserId(),
              nutritionEvent.getMealType(),
              nutritionEvent.getCalories(),
              nutritionEvent.getEventType());

      // Call AI processing for nutrition events
      processNutritionEvent(nutritionEvent);

    } catch (InvalidProtocolBufferException e) {
      log.error("Error deserializing NutritionEvent: {}", e.getMessage());
    }
  }

  // ================= AI Processing Placeholders =================

  private void processActivityEvent(ActivityEvent activity, UserEvent user) {
    // TODO: Implement AI logic here
    // e.g., recommend workout adjustments, detect anomalies, send alerts
    log.info("AI processing ActivityEvent for user {}", user.getUserId());
  }

  private void processUserEvent(UserEvent user) {
    // TODO: Implement AI logic for user data
    // e.g., personalize nutrition/workout plans
    log.info("AI processing UserEvent for user {}", user.getUserId());
  }

  private void processNutritionEvent(NutritionEvent nutrition) {
    // TODO: Implement AI logic for nutrition data
    // e.g., detect calorie imbalance, suggest diet changes
    log.info("AI processing NutritionEvent for user {}", nutrition.getUserId());
  }
}
