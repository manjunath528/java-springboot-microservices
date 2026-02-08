package com.pm.userservice.kafka;

import com.pm.userservice.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import user.events.UserEvent;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {

    @Mock
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @Captor
    private ArgumentCaptor<byte[]> payloadCaptor;

    @Test
    void sendEventPublishesUserCreatedMessage() throws Exception {
        KafkaProducer producer = new KafkaProducer(kafkaTemplate);
        User user = sampleUser();

        producer.sendEvent(user);

        verify(kafkaTemplate).send(org.mockito.ArgumentMatchers.eq("user"), payloadCaptor.capture());
        UserEvent event = UserEvent.parseFrom(payloadCaptor.getValue());
        assertThat(event.getUserId()).isEqualTo(user.getId().toString());
        assertThat(event.getName()).isEqualTo(user.getName());
        assertThat(event.getEmail()).isEqualTo(user.getEmail());
        assertThat(event.getEventType()).isEqualTo("USER_CREATED");
    }

    @Test
    void sendEventSwallowsKafkaErrors() {
        KafkaProducer producer = new KafkaProducer(kafkaTemplate);
        User user = sampleUser();

        doThrow(new RuntimeException("Kafka down"))
                .when(kafkaTemplate)
                .send(org.mockito.ArgumentMatchers.eq("user"), org.mockito.ArgumentMatchers.any(byte[].class));

        producer.sendEvent(user);

        verify(kafkaTemplate).send(org.mockito.ArgumentMatchers.eq("user"), org.mockito.ArgumentMatchers.any(byte[].class));
    }

    @Test
    void sendUserDeletedEventPublishesDeleteMessage() throws Exception {
        KafkaProducer producer = new KafkaProducer(kafkaTemplate);
        User user = sampleUser();

        producer.sendUserDeletedEvent(user);

        verify(kafkaTemplate).send(org.mockito.ArgumentMatchers.eq("user"), payloadCaptor.capture());
        UserEvent event = UserEvent.parseFrom(payloadCaptor.getValue());
        assertThat(event.getEventType()).isEqualTo("USER_DELETED");
        assertThat(event.getUserId()).isEqualTo(user.getId().toString());
    }

    @Test
    void sendUserDeletedEventSwallowsKafkaErrors() {
        KafkaProducer producer = new KafkaProducer(kafkaTemplate);
        User user = sampleUser();

        doThrow(new RuntimeException("Kafka down"))
                .when(kafkaTemplate)
                .send(org.mockito.ArgumentMatchers.eq("user"), org.mockito.ArgumentMatchers.any(byte[].class));

        producer.sendUserDeletedEvent(user);

        verify(kafkaTemplate).send(org.mockito.ArgumentMatchers.eq("user"), org.mockito.ArgumentMatchers.any(byte[].class));
    }

    private static User sampleUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Alex");
        user.setEmail("alex@example.com");
        user.setAddress("123 Main St");
        return user;
    }
}
