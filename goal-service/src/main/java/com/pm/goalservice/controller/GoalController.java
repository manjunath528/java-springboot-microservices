package com.pm.goalservice.controller;

import com.pm.goalservice.dto.CreateGoalRequest;
import com.pm.goalservice.dto.GoalResponse;
import com.pm.goalservice.service.GoalService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/goals")
public class GoalController {

    private final GoalService service;

    public GoalController(GoalService service) {
        this.service = service;
    }

    @PostMapping
    public GoalResponse create(@RequestBody CreateGoalRequest request) {
        return GoalResponse.from(service.createGoal(request));
    }

    @GetMapping("/active/{userId}")
    public GoalResponse getActive(@PathVariable UUID userId) {
        return GoalResponse.from(service.getActiveGoal(userId));
    }
}

