package com.pm.nutritionservice.kafka;

import com.pm.nutritionservice.model.Nutrition;
import nutrition.events.NutritionEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
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
    void sendNutritionCreatedEventPublishesPayload() throws Exception {
        KafkaProducer producer = new KafkaProducer(kafkaTemplate);
        Nutrition nutrition = sampleNutrition();

        producer.sendNutritionCreatedEvent(nutrition);

        verify(kafkaTemplate).send(org.mockito.ArgumentMatchers.eq("nutrition"), payloadCaptor.capture());
        NutritionEvent event = NutritionEvent.parseFrom(payloadCaptor.getValue());
        assertThat(event.getId()).isEqualTo(nutrition.getId().toString());
        assertThat(event.getUserId()).isEqualTo(nutrition.getUserId().toString());
        assertThat(event.getCalories()).isEqualTo(nutrition.getCalories());
        assertThat(event.getProtein()).isEqualTo(nutrition.getProtein());
        assertThat(event.getCarbs()).isEqualTo(nutrition.getCarbs());
        assertThat(event.getFat()).isEqualTo(nutrition.getFat());
        assertThat(event.getEventType()).isEqualTo(String.valueOf(NutritionEvent.EventType.NUTRITION_CREATED));
    }

    @Test
    void sendNutritionCreatedEventSwallowsKafkaErrors() {
        KafkaProducer producer = new KafkaProducer(kafkaTemplate);
        Nutrition nutrition = sampleNutrition();

        doThrow(new RuntimeException("Kafka down"))
                .when(kafkaTemplate)
                .send(org.mockito.ArgumentMatchers.eq("nutrition"), org.mockito.ArgumentMatchers.any(byte[].class));

        producer.sendNutritionCreatedEvent(nutrition);

        verify(kafkaTemplate).send(org.mockito.ArgumentMatchers.eq("nutrition"), org.mockito.ArgumentMatchers.any(byte[].class));
    }

    private static Nutrition sampleNutrition() {
        Nutrition nutrition = new Nutrition();
        nutrition.setId(UUID.randomUUID());
        nutrition.setUserId(UUID.randomUUID());
        nutrition.setMealType("LUNCH");
        nutrition.setCalories(600);
        nutrition.setProtein(30);
        nutrition.setCarbs(70);
        nutrition.setFat(20);
        nutrition.setDate(LocalDate.now());
        return nutrition;
    }
}
