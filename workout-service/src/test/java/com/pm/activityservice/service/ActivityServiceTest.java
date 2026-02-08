package com.pm.activityservice.service;

import com.pm.activityservice.dto.ActivityRequest;
import com.pm.activityservice.dto.ActivityResponse;
import com.pm.activityservice.exception.ActivityNotFoundException;
import com.pm.activityservice.kafka.KafkaProducer;
import com.pm.activityservice.model.Activity;
import com.pm.activityservice.model.ActivityStatus;
import com.pm.activityservice.model.WorkoutType;
import com.pm.activityservice.repository.ActivityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private ActivityService activityService;

    @Test
    void createActivitySavesAndPublishesEvent() {
        ActivityRequest request = sampleRequest();
        Activity saved = sampleActivity();
        when(activityRepository.save(any(Activity.class))).thenReturn(saved);

        ActivityResponse response = activityService.createActivity(request);

        assertThat(response.getId()).isEqualTo(saved.getId());
        assertThat(response.getStatus()).isEqualTo(saved.getStatus());
        verify(kafkaProducer).sendActivityCreatedEvent(saved);
    }

    @Test
    void updateStatusThrowsWhenNotFound() {
        UUID activityId = UUID.randomUUID();
        when(activityRepository.findById(activityId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> activityService.updateStatus(activityId, "COMPLETED"))
                .isInstanceOf(ActivityNotFoundException.class);
    }

    @Test
    void updateStatusUpdatesStatusCaseInsensitive() {
        UUID activityId = UUID.randomUUID();
        Activity activity = sampleActivity();
        activity.setId(activityId);
        when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));

        ActivityResponse response = activityService.updateStatus(activityId, "completed");

        assertThat(response.getStatus()).isEqualTo(ActivityStatus.COMPLETED);
        assertThat(activity.getStatus()).isEqualTo(ActivityStatus.COMPLETED);
    }

    @Test
    void updateStatusThrowsForInvalidStatus() {
        UUID activityId = UUID.randomUUID();
        Activity activity = sampleActivity();
        when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));

        assertThatThrownBy(() -> activityService.updateStatus(activityId, "UNKNOWN"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getUserActivitiesReturnsMappedResults() {
        UUID userId = UUID.randomUUID();
        when(activityRepository.findByUserId(userId))
                .thenReturn(List.of(sampleActivity(), sampleActivity()));

        List<ActivityResponse> responses = activityService.getUserActivities(userId);

        assertThat(responses).hasSize(2);
    }

    @Test
    void getUserActivitiesByDateReturnsMappedResults() {
        UUID userId = UUID.randomUUID();
        LocalDate date = LocalDate.now();
        when(activityRepository.findByUserIdAndActivityDate(userId, date))
                .thenReturn(List.of(sampleActivity()));

        List<ActivityResponse> responses = activityService.getUserActivitiesByDate(userId, date);

        assertThat(responses).hasSize(1);
    }

    @Test
    void deleteActivitiesForUserDeletesByUserId() {
        UUID userId = UUID.randomUUID();

        activityService.deleteActivitiesForUser(userId.toString());

        verify(activityRepository).deleteByUserId(userId);
    }

    @Test
    void deleteActivitiesForUserThrowsOnInvalidUuid() {
        assertThatThrownBy(() -> activityService.deleteActivitiesForUser("not-a-uuid"))
                .isInstanceOf(IllegalArgumentException.class);
        verify(activityRepository, never()).deleteByUserId(any());
    }

    private static ActivityRequest sampleRequest() {
        ActivityRequest request = new ActivityRequest();
        request.setUserId(UUID.randomUUID());
        request.setWorkoutType(WorkoutType.RUNNING);
        request.setDurationMinutes(30);
        request.setCaloriesBurned(250);
        request.setActivityDate(LocalDate.now());
        request.setStatus("COMPLETED");
        return request;
    }

    private static Activity sampleActivity() {
        Activity activity = new Activity();
        activity.setId(UUID.randomUUID());
        activity.setUserId(UUID.randomUUID());
        activity.setWorkoutType(WorkoutType.CARDIO);
        activity.setDurationMinutes(45);
        activity.setCaloriesBurned(400);
        activity.setActivityDate(LocalDate.now());
        activity.setStatus(ActivityStatus.COMPLETED);
        activity.setCreatedAt(LocalDateTime.now());
        return activity;
    }
}
