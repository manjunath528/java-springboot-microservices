package com.pm.goalservice.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "goals")
public class Goal {

    @Id
    @GeneratedValue
    private UUID goalId;

    private UUID userId;

    @Enumerated(EnumType.STRING)
    private GoalType goalType;

    private int targetValue;

    private LocalDate targetDate;

    @Enumerated(EnumType.STRING)
    private GoalStatus status;

    private Instant createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
        this.status = GoalStatus.ACTIVE;
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

    public GoalStatus getStatus() {
        return status;
    }

    public void setStatus(GoalStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "goalId=" + goalId +
                ", userId=" + userId +
                ", goalType=" + goalType +
                ", targetValue=" + targetValue +
                ", targetDate=" + targetDate +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return targetValue == goal.targetValue && Objects.equals(goalId, goal.goalId) && Objects.equals(userId, goal.userId) && goalType == goal.goalType && Objects.equals(targetDate, goal.targetDate) && status == goal.status && Objects.equals(createdAt, goal.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goalId, userId, goalType, targetValue, targetDate, status, createdAt);
    }
}
