package com.pm.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import users.events.UserEvent;

@Service
public class KafkaConsumer {

  private static final Logger log = LoggerFactory.getLogger(
      KafkaConsumer.class);

  @KafkaListener(topics="user", groupId = "analytics-service")
  public void consumeEvent(byte[] event) {
    try {
      UserEvent userEvent = UserEvent.parseFrom(event);
      // ... perform any business related to analytics here

      log.info("Received User Event: [UserId={},User Name={},User Email={}]",
              userEvent.getUserId(),
              userEvent.getName(),
              userEvent.getEmail());
    } catch (InvalidProtocolBufferException e) {
      log.error("Error deserializing event {}", e.getMessage());
    }
  }
}
