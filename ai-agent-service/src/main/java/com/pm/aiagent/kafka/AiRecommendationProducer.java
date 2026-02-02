package com.pm.aiagent.kafka;

import com.pm.aiagent.model.AiRecommendation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AiRecommendationProducer {

    private final KafkaTemplate<String, AiRecommendation> kafkaTemplate;
    private final String topic;

    public AiRecommendationProducer(
            KafkaTemplate<String, AiRecommendation> kafkaTemplate,
            @Value("${ai.kafka.topic.recommendations}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(AiRecommendation recommendation) {
        kafkaTemplate.send(topic, recommendation.userId(), recommendation);
    }
}
