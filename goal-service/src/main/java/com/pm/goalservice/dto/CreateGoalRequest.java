package com.pm.goalservice.dto;

import com.pm.goalservice.model.GoalType;

import java.time.LocalDate;
import java.util.UUID;

public class CreateGoalRequest {

    private UUID userId;
    private GoalType goalType;
    private int targetValue;
    private LocalDate targetDate;

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
        return "CreateGoalRequest{" +
                "userId=" + userId +
                ", goalType=" + goalType +
                ", targetValue=" + targetValue +
                ", targetDate=" + targetDate +
                '}';
    }
}
