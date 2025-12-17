package com.pm.activityservice.controller;

import com.pm.activityservice.dto.ActivityRequest;
import com.pm.activityservice.dto.ActivityResponse;
import com.pm.activityservice.service.ActivityService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    private static final Logger log = LoggerFactory.getLogger(ActivityController.class);

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping
    public ResponseEntity<ActivityResponse> createActivity(
            @Valid @RequestBody ActivityRequest request) {
        log.info("Create Activity request received -> userId->{}, workoutType->{}, activityDate->{}",
                request.getUserId(),
                request.getWorkoutType(),
                request.getActivityDate());
        ActivityResponse response = activityService.createActivity(request);
        log.info("Activity created successfully -> activityId->{}, userId->{}",
                response.getId(),
                response.getUserId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{activityId}/status")
    public ResponseEntity<ActivityResponse> updateStatus(
            @PathVariable UUID activityId,
            @RequestParam String status) {
        log.info("Update Activity Status request -> activityId->{}, newStatus->{}",
                activityId,
                status);
        ActivityResponse response = activityService.updateStatus(activityId, status);
        log.info("Activity status updated -> activityId->{}, status->{}",
                response.getId(),
                response.getStatus());

        return ResponseEntity.ok(response);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActivityResponse>> getUserActivities(
            @PathVariable UUID userId) {
        log.info("Fetch activities request -> userId->{}", userId);
        List<ActivityResponse> activities = activityService.getUserActivities(userId);
        log.info("Activities fetched -> userId->{}, count->{}",
                userId,
                activities.size());
        return ResponseEntity.ok(activities);
    }
    @GetMapping("/user/{userId}/date")
    public ResponseEntity<List<ActivityResponse>> getUserActivitiesByDate(
            @PathVariable UUID userId,
            @RequestParam LocalDate date) {
        log.info("Fetch activities by date request -> userId->{}, date->{}",
                userId,
                date);
        List<ActivityResponse> activities =
                activityService.getUserActivitiesByDate(userId, date);
        log.info("Activities fetched by date -> userId->{}, date->{}, count->{}",
                userId,
                date,
                activities.size());
        return ResponseEntity.ok(activities);
    }
}
