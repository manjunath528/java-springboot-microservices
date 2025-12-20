package com.pm.activityservice.controller;

import com.pm.activityservice.dto.ActivityRequest;
import com.pm.activityservice.dto.ActivityResponse;
import com.pm.activityservice.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Activity", description = "API for managing Activities")
public class ActivityController {

    private static final Logger log = LoggerFactory.getLogger(ActivityController.class);

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    // ================== CREATE ACTIVITY ==================
    @PostMapping
    @Operation(summary = "Create a new Activity")
    public ResponseEntity<ActivityResponse> createActivity(@Valid @RequestBody ActivityRequest request) {
        log.info("Request received -> Create Activity -> userId->{}, workoutType->{}, activityDate->{}",
                request.getUserId(),
                request.getWorkoutType(),
                request.getActivityDate());

        ActivityResponse response = activityService.createActivity(request);

        log.info("Activity created successfully -> activityId->{}, userId->{}",
                response.getId(),
                response.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ================== UPDATE ACTIVITY STATUS ==================
    @PatchMapping("/{activityId}/status")
    @Operation(summary = "Update Activity Status")
    public ResponseEntity<ActivityResponse> updateStatus(@PathVariable UUID activityId,
                                                         @RequestParam String status) {
        log.info("Request received -> Update Activity Status -> activityId->{}, newStatus->{}", activityId, status);

        ActivityResponse response = activityService.updateStatus(activityId, status);

        log.info("Activity status updated -> activityId->{}, status->{}", response.getId(), response.getStatus());

        return ResponseEntity.ok(response);
    }

    // ================== GET USER ACTIVITIES ==================
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get Activities for a User")
    public ResponseEntity<List<ActivityResponse>> getUserActivities(@PathVariable UUID userId) {
        log.info("Request received -> Fetch Activities -> userId->{}", userId);

        List<ActivityResponse> activities = activityService.getUserActivities(userId);

        log.info("Activities fetched -> userId->{}, count->{}", userId, activities.size());

        return ResponseEntity.ok(activities);
    }

    // ================== GET USER ACTIVITIES BY DATE ==================
    @GetMapping("/user/{userId}/date")
    @Operation(summary = "Get Activities for a User by Date")
    public ResponseEntity<List<ActivityResponse>> getUserActivitiesByDate(@PathVariable UUID userId,
                                                                          @RequestParam LocalDate date) {
        log.info("Request received -> Fetch Activities by Date -> userId->{}, date->{}", userId, date);

        List<ActivityResponse> activities = activityService.getUserActivitiesByDate(userId, date);

        log.info("Activities fetched by date -> userId->{}, date->{}, count->{}", userId, date, activities.size());

        return ResponseEntity.ok(activities);
    }

}
