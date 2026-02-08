package com.pm.userservice.kafka;
import com.pm.userservice.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import user.events.UserEvent;

@Service
public class KafkaProducer {

  private static final Logger log = LoggerFactory.getLogger(
      KafkaProducer.class);
  private final KafkaTemplate<String, byte[]> kafkaTemplate;

  public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendEvent(User user) {
    UserEvent event = UserEvent.newBuilder()
        .setUserId(user.getId().toString())
        .setName(user.getName())
        .setEmail(user.getEmail())
        .setEventType("USER_CREATED")
        .build();

    try {
      kafkaTemplate.send("user", event.toByteArray());
      log.info("USER_CREATED event published for userId={}", user.getId());
    } catch (Exception e) {
      log.error("Failed to publish USER_CREATED event for userId={}", user.getId(), e);
    }
  }
  public void sendUserDeletedEvent(User user) {
    UserEvent event = UserEvent.newBuilder()
            .setUserId(user.getId().toString())
            .setName(user.getName())
            .setEmail(user.getEmail())
            .setEventType("USER_DELETED")
            .build();

    try {
      kafkaTemplate.send("user", event.toByteArray());
      log.info("USER_DELETED event published for userId={}", user.getId());
    } catch (Exception e) {
      log.error("Failed to publish USER_DELETED event for userId={}", user.getId(), e);
    }
  }
}
