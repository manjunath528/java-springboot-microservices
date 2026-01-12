package com.pm.aiagent.kafka;
import com.pm.aiagent.model.AiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AiProducer {

    @Autowired
    private KafkaTemplate<String, AiResponse> kafkaTemplate;

    private static final String TOPIC = "ai-recommendations";

    public void sendRecommendation(AiResponse response) {
        kafkaTemplate.send(TOPIC, response.getUserId(), response);
    }
}

