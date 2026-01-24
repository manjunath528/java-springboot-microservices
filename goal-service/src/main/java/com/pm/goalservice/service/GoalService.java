package com.pm.goalservice.service;

import com.pm.goalservice.dto.CreateGoalRequest;
import com.pm.goalservice.kafka.GoalEventProducer;
import com.pm.goalservice.model.Goal;
import com.pm.goalservice.model.GoalStatus;
import com.pm.goalservice.repository.GoalRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GoalService {

    private final GoalRepository repository;
    private final GoalEventProducer producer;

    public GoalService(GoalRepository repository, GoalEventProducer producer) {
        this.repository = repository;
        this.producer = producer;
    }

    public Goal createGoal(CreateGoalRequest request) {

        repository.findByUserIdAndStatus(request.getUserId(), GoalStatus.ACTIVE)
                .ifPresent(g -> {
                    throw new IllegalStateException("Active goal already exists");
                });

        Goal goal = new Goal();
        goal.setUserId(request.getUserId());
        goal.setGoalType(request.getGoalType());
        goal.setTargetValue(request.getTargetValue());
        goal.setTargetDate(request.getTargetDate());

        Goal saved = repository.save(goal);
        producer.publishGoalCreated(saved);

        return saved;
    }

    public Goal getActiveGoal(UUID userId) {
        return repository.findByUserIdAndStatus(userId, GoalStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active goal"));
    }
}
