package com.pm.activityservice.service;

import com.pm.activityservice.dto.ActivityRequest;
import com.pm.activityservice.dto.ActivityResponse;
import com.pm.activityservice.mapper.ActivityMapper;
import com.pm.activityservice.model.Activity;
import com.pm.activityservice.repository.ActivityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public ActivityResponse createActivity(ActivityRequest request) {
        Activity activity = ActivityMapper.toEntity(request);
        Activity saved = activityRepository.save(activity);
        return ActivityMapper.toDTO(saved);
    }

    public ActivityResponse updateStatus(UUID activityId, String status) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        activity.setStatus(Enum.valueOf(
                com.pm.activityservice.model.ActivityStatus.class, status));

        return ActivityMapper.toDTO(activity);
    }

    public List<ActivityResponse> getUserActivities(UUID userId) {
        return activityRepository.findByUserId(userId)
                .stream()
                .map(ActivityMapper::toDTO)
                .toList();
    }

    public List<ActivityResponse> getUserActivitiesByDate(UUID userId, LocalDate date) {
        return activityRepository.findByUserIdAndActivityDate(userId, date)
                .stream()
                .map(ActivityMapper::toDTO)
                .toList();
    }
}
