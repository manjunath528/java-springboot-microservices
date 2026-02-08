package com.pm.activityservice.mapper;

import com.pm.activityservice.dto.ActivityRequest;
import com.pm.activityservice.dto.ActivityResponse;
import com.pm.activityservice.model.Activity;
import com.pm.activityservice.model.ActivityStatus;
import com.pm.activityservice.model.WorkoutType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ActivityMapperTest {

    @Test
    void toEntityDefaultsStatusToPlannedWhenMissing() {
        ActivityRequest request = new ActivityRequest();
        request.setUserId(UUID.randomUUID());
        request.setWorkoutType(WorkoutType.YOGA);
        request.setDurationMinutes(60);
        request.setCaloriesBurned(200);
        request.setActivityDate(LocalDate.now());

        Activity activity = ActivityMapper.toEntity(request);

        assertThat(activity.getStatus()).isEqualTo(ActivityStatus.PLANNED);
    }

    @Test
    void toEntityUsesProvidedStatusWhenPresent() {
        ActivityRequest request = new ActivityRequest();
        request.setUserId(UUID.randomUUID());
        request.setWorkoutType(WorkoutType.HIIT);
        request.setDurationMinutes(20);
        request.setCaloriesBurned(300);
        request.setActivityDate(LocalDate.now());
        request.setStatus("COMPLETED");

        Activity activity = ActivityMapper.toEntity(request);

        assertThat(activity.getStatus()).isEqualTo(ActivityStatus.COMPLETED);
    }

    @Test
    void toDtoMapsAllFields() {
        Activity activity = new Activity();
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        activity.setId(id);
        activity.setUserId(userId);
        activity.setWorkoutType(WorkoutType.RUNNING);
        activity.setDurationMinutes(30);
        activity.setCaloriesBurned(250);
        activity.setActivityDate(LocalDate.parse("2024-01-10"));
        activity.setStatus(ActivityStatus.COMPLETED);
        activity.setCreatedAt(LocalDateTime.parse("2024-01-10T10:15:30"));

        ActivityResponse response = ActivityMapper.toDTO(activity);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getWorkoutType()).isEqualTo(WorkoutType.RUNNING);
        assertThat(response.getDurationMinutes()).isEqualTo(30);
        assertThat(response.getCaloriesBurned()).isEqualTo(250);
        assertThat(response.getActivityDate()).isEqualTo(LocalDate.parse("2024-01-10"));
        assertThat(response.getStatus()).isEqualTo(ActivityStatus.COMPLETED);
        assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.parse("2024-01-10T10:15:30"));
    }
}
