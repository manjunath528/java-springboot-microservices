package com.pm.activityservice.mapper;

import com.pm.activityservice.dto.ActivityResponse;
import com.pm.activityservice.dto.ActivityRequest;
import com.pm.activityservice.model.Activity;
import com.pm.activityservice.model.ActivityStatus;

public class ActivityMapper {

    public static Activity toEntity(ActivityRequest dto) {
        Activity activity = new Activity();
        activity.setUserId(dto.getUserId());
        activity.setWorkoutType(dto.getWorkoutType());
        activity.setDurationMinutes(dto.getDurationMinutes());
        activity.setCaloriesBurned(dto.getCaloriesBurned());
        activity.setActivityDate(dto.getActivityDate());
        activity.setStatus(ActivityStatus.COMPLETED);
        if (dto.getStatus() != null) {
            activity.setStatus(ActivityStatus.valueOf(dto.getStatus()));
        } else {
            activity.setStatus(ActivityStatus.PLANNED);
        }

        return activity;
    }

    public static ActivityResponse toDTO(Activity activity) {
        ActivityResponse dto = new ActivityResponse();
        dto.setId(activity.getId());
        dto.setUserId(activity.getUserId());
        dto.setWorkoutType(activity.getWorkoutType());
        dto.setDurationMinutes(activity.getDurationMinutes());
        dto.setCaloriesBurned(activity.getCaloriesBurned());
        dto.setActivityDate(activity.getActivityDate());
        dto.setStatus(activity.getStatus());
        dto.setCreatedAt(activity.getCreatedAt());
        return dto;
    }
}
