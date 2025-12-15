package com.pm.activityservice.controller;

import com.pm.activityservice.dto.ActivityRequest;
import com.pm.activityservice.dto.ActivityResponse;
import com.pm.activityservice.service.ActivityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping
    public ResponseEntity<ActivityResponse> createActivity(
            @Valid @RequestBody ActivityRequest request) {

        return new ResponseEntity<>(
                activityService.createActivity(request),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{activityId}/status")
    public ResponseEntity<ActivityResponse> updateStatus(
            @PathVariable UUID activityId,
            @RequestParam String status) {

        return ResponseEntity.ok(
                activityService.updateStatus(activityId, status)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActivityResponse>> getUserActivities(
            @PathVariable UUID userId) {

        return ResponseEntity.ok(
                activityService.getUserActivities(userId)
        );
    }

    @GetMapping("/user/{userId}/date")
    public ResponseEntity<List<ActivityResponse>> getUserActivitiesByDate(
            @PathVariable UUID userId,
            @RequestParam LocalDate date) {

        return ResponseEntity.ok(
                activityService.getUserActivitiesByDate(userId, date)
        );
    }
}
