package com.pm.activityservice.service;

import com.pm.activityservice.dto.ActivityRequest;
import com.pm.activityservice.dto.ActivityResponse;
import com.pm.activityservice.exception.ActivityNotFoundException;
import com.pm.activityservice.kafka.KafkaProducer;
import com.pm.activityservice.mapper.ActivityMapper;
import com.pm.activityservice.model.Activity;
import com.pm.activityservice.model.ActivityStatus;
import com.pm.activityservice.repository.ActivityRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final ActivityRepository activityRepository;
    private final KafkaProducer kafkaProducer;

    public ActivityService(ActivityRepository activityRepository, KafkaProducer kafkaProducer) {
        this.activityRepository = activityRepository;
        this.kafkaProducer = kafkaProducer;
    }

    // ================== CREATE ACTIVITY ==================
    public ActivityResponse createActivity(ActivityRequest request) {
        log.info("Creating activity for userId->{}", request.getUserId());

        Activity activity = ActivityMapper.toEntity(request);
        Activity saved = activityRepository.save(activity);

        log.info("Activity created successfully -> activityId->{}, userId->{}", saved.getId(), saved.getUserId());

        kafkaProducer.sendActivityCreatedEvent(saved);
        log.info("ActivityCreated event sent to Kafka -> activityId->{}", saved.getId());

        return ActivityMapper.toDTO(saved);
    }

    // ================== UPDATE STATUS ==================
    public ActivityResponse updateStatus(UUID activityId, String status) {
        log.info("Updating activity status -> activityId->{}, newStatus->{}", activityId, status);

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> {
                    log.error("Activity not found -> activityId->{}", activityId);
                    return new ActivityNotFoundException("Activity not found with ID: " + activityId);
                });

        activity.setStatus(ActivityStatus.valueOf(status.toUpperCase()));
        log.info("Activity status updated -> activityId->{}, status->{}", activityId, activity.getStatus());

        return ActivityMapper.toDTO(activity);
    }

    // ================== GET USER ACTIVITIES ==================
    public List<ActivityResponse> getUserActivities(UUID userId) {
        log.info("Fetching activities for userId->{}", userId);

        List<ActivityResponse> responses = activityRepository.findByUserId(userId)
                .stream()
                .map(ActivityMapper::toDTO)
                .toList();

        log.info("Fetched {} activities for userId->{}", responses.size(), userId);
        return responses;
    }

    // ================== GET USER ACTIVITIES BY DATE ==================
    public List<ActivityResponse> getUserActivitiesByDate(UUID userId, LocalDate date) {
        log.info("Fetching activities for userId->{} on date->{}", userId, date);

        List<ActivityResponse> responses = activityRepository.findByUserIdAndActivityDate(userId, date)
                .stream()
                .map(ActivityMapper::toDTO)
                .toList();

        log.info("Fetched {} activities for userId->{} on date->{}", responses.size(), userId, date);
        return responses;
    }

    // ================== DELETE USER ACTIVITIES ==================
    @Transactional
    public void deleteActivitiesForUser(String userId) {
        log.info("Deleting all activities for userId->{}", userId);
        UUID userIdUUID = UUID.fromString(userId);
        activityRepository.deleteByUserId(userIdUUID);

        log.info("Successfully deleted all activities for userId->{}", userId);
    }
}
