package com.pm.goalservice.kafka;

import com.pm.goalservice.dto.GoalCreatedEvent;
import com.pm.goalservice.model.Goal;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class GoalEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public GoalEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishGoalCreated(Goal goal) {
        GoalCreatedEvent event = new GoalCreatedEvent(
                goal.getGoalId(),
                goal.getUserId(),
                goal.getGoalType(),
                goal.getTargetValue(),
                goal.getTargetDate()
        );

        kafkaTemplate.send("goal.created", goal.getUserId().toString(), event);
    }
}

