package com.pm.activityservice.mapper;

import com.pm.activityservice.dto.ActivityResponseDTO;
import com.pm.activityservice.dto.CreateActivityRequest;
import com.pm.activityservice.model.Activity;
import com.pm.activityservice.model.ActivityStatus;

public class ActivityMapper {

    public static Activity toEntity(CreateActivityRequest dto) {
        Activity activity = new Activity();
        activity.setUserId(dto.getUserId());
        activity.setWorkoutType(dto.getWorkoutType());
        activity.setDurationMinutes(dto.getDurationMinutes());
        activity.setCaloriesBurned(dto.getCaloriesBurned());
        activity.setActivityDate(dto.getActivityDate());
        activity.setStatus(ActivityStatus.COMPLETED);
        return activity;
    }

    public static ActivityResponseDTO toDTO(Activity activity) {
        ActivityResponseDTO dto = new ActivityResponseDTO();
        dto.setId(activity.getId());
        dto.setWorkoutType(activity.getWorkoutType());
        dto.setDurationMinutes(activity.getDurationMinutes());
        dto.setCaloriesBurned(activity.getCaloriesBurned());
        dto.setActivityDate(activity.getActivityDate());
        dto.setStatus(activity.getStatus());
        return dto;
    }
}
