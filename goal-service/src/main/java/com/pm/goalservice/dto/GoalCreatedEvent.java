package com.pm.goalservice.dto;

import com.pm.goalservice.model.GoalType;

import java.time.LocalDate;
import java.util.UUID;

public class GoalCreatedEvent {

    private UUID goalId;
    private UUID userId;
    private GoalType goalType;
    private int targetValue;
    private LocalDate targetDate;

    public GoalCreatedEvent(UUID goalId, UUID userId, GoalType goalType, int targetValue, LocalDate targetDate) {
        this.goalId = goalId;
        this.userId = userId;
        this.goalType = goalType;
        this.targetValue = targetValue;
        this.targetDate = targetDate;
    }

    public UUID getGoalId() {
        return goalId;
    }

    public void setGoalId(UUID goalId) {
        this.goalId = goalId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public GoalType getGoalType() {
        return goalType;
    }

    public void setGoalType(GoalType goalType) {
        this.goalType = goalType;
    }

    public int getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(int targetValue) {
        this.targetValue = targetValue;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    @Override
    public String toString() {
        return "GoalCreatedEvent{" +
                "goalId=" + goalId +
                ", userId=" + userId +
                ", goalType=" + goalType +
                ", targetValue=" + targetValue +
                ", targetDate=" + targetDate +
                '}';
    }
}

