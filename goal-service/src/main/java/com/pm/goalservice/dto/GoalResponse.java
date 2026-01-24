package com.pm.goalservice.dto;

import com.pm.goalservice.model.Goal;
import com.pm.goalservice.model.GoalStatus;
import com.pm.goalservice.model.GoalType;

import java.time.LocalDate;
import java.util.UUID;

public class GoalResponse {

    private UUID goalId;
    private UUID userId;
    private GoalType goalType;
    private int targetValue;
    private LocalDate targetDate;
    private GoalStatus status;

    public static GoalResponse from(Goal goal) {
        GoalResponse r = new GoalResponse();
        r.goalId = goal.getGoalId();
        r.userId = goal.getUserId();
        r.goalType = goal.getGoalType();
        r.targetValue = goal.getTargetValue();
        r.targetDate = goal.getTargetDate();
        r.status = goal.getStatus();
        return r;
    }
}

