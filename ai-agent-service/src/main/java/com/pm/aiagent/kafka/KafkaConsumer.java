package com.pm.aiagent.kafka;

import activity.events.ActivityEvent;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pm.aiagent.model.AiRecommendation;
import com.pm.aiagent.model.UserContext;
import com.pm.aiagent.service.AiEngine;
import com.pm.aiagent.store.UserContextStore;
import nutrition.events.NutritionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import users.events.UserEvent;

import java.time.Instant;

@Service
public class KafkaConsumer {

  private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

  private final UserContextStore userStore;
  private final AiEngine aiEngine;
  private final AiRecommendationProducer producer;

  public KafkaConsumer(UserContextStore userStore, AiEngine aiEngine, AiRecommendationProducer producer) {
    this.userStore = userStore;
    this.aiEngine = aiEngine;
    this.producer = producer;
  }

  @KafkaListener(topics = "user", groupId = "ai-agent-service")
  public void consumeUserEvent(byte[] event) {
    try {
      UserEvent userEvent = UserEvent.parseFrom(event);

      log.info("Received User Event: [UserId={}, Name={}, Email={}, EventType={}]",
              userEvent.getUserId(), userEvent.getName(), userEvent.getEmail(), userEvent.getEventType());

      userStore.put(new UserContext(
              userEvent.getUserId(),
              userEvent.getName(),
              userEvent.getEmail()
      ));

    } catch (InvalidProtocolBufferException e) {
      log.error("Error deserializing UserEvent: {}", e.getMessage());
    }
  }

  @KafkaListener(topics = "activity", groupId = "ai-agent-service")
  public void consumeActivityEvent(byte[] event) {
    try {
      ActivityEvent a = ActivityEvent.parseFrom(event);

      log.info("Received Activity Event: [ActivityId={}, UserId={}, EventType={}, ActivityType={}, Duration={}, Calories={}]",
              a.getActivityId(), a.getUserId(), a.getEventType(), a.getActivityType(),
              a.getDurationMinutes(), a.getCaloriesBurned());

      UserContext user = userStore.get(a.getUserId())
              .orElse(new UserContext(a.getUserId(), "Unknown", "unknown@example.com"));

      String msg = aiEngine.coachForActivity(a, user);

      AiRecommendation out = new AiRecommendation(
              user.userId(),
              "ACTIVITY",
              msg,
              "INFO",
              Instant.now()
      );

      producer.publish(out);
      log.info("Published AI recommendation for user {} to ai-recommendations", user.userId());

    } catch (InvalidProtocolBufferException e) {
      log.error("Error deserializing ActivityEvent: {}", e.getMessage());
    } catch (Exception e) {
      log.error("AI processing failed for ActivityEvent: {}", e.getMessage(), e);
    }
  }

  @KafkaListener(topics = "nutrition", groupId = "ai-agent-service")
  public void consumeNutritionEvent(byte[] event) {
    try {
      NutritionEvent n = NutritionEvent.parseFrom(event);

      log.info("Received Nutrition Event: [Id={}, UserId={}, MealType={}, Calories={}, EventType={}]",
              n.getId(), n.getUserId(), n.getMealType(), n.getCalories(), n.getEventType());

      UserContext user = userStore.get(n.getUserId())
              .orElse(new UserContext(n.getUserId(), "Unknown", "unknown@example.com"));

      String msg = aiEngine.coachForNutrition(n, user);

      AiRecommendation out = new AiRecommendation(
              user.userId(),
              "NUTRITION",
              msg,
              n.getCalories() > 800 ? "WARNING" : "INFO",
              Instant.now()
      );

      producer.publish(out);
      log.info("Published AI recommendation for user {} to ai-recommendations", user.userId());

    } catch (InvalidProtocolBufferException e) {
      log.error("Error deserializing NutritionEvent: {}", e.getMessage());
    } catch (Exception e) {
      log.error("AI processing failed for NutritionEvent: {}", e.getMessage(), e);
    }
  }
}
