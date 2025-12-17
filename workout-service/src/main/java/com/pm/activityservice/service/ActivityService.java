package com.pm.activityservice.service;

import com.pm.activityservice.dto.ActivityRequest;
import com.pm.activityservice.dto.ActivityResponse;
import com.pm.activityservice.kafka.KafkaProducer;
import com.pm.activityservice.mapper.ActivityMapper;
import com.pm.activityservice.model.Activity;
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

    private final ActivityRepository activityRepository;
    private final KafkaProducer kafkaProducer;

    private final Logger log = LoggerFactory.getLogger(ActivityService.class);

    public ActivityService(ActivityRepository activityRepository, KafkaProducer kafkaProducer) {
        this.activityRepository = activityRepository;
        this.kafkaProducer = kafkaProducer;
    }

    public ActivityResponse createActivity(ActivityRequest request) {
        log.info("Creating activity for userId->{}", request.getUserId());

        Activity activity = ActivityMapper.toEntity(request);
        Activity saved = activityRepository.save(activity);

        log.info("Activity created successfully. activityId->{}, userId->{}",
                saved.getId(), saved.getUserId());

        kafkaProducer.sendActivityCreatedEvent(saved);
        log.info("ActivityCreated event sent to Kafka. activityId->{}", saved.getId());

        return ActivityMapper.toDTO(saved);
    }

    public ActivityResponse updateStatus(UUID activityId, String status) {
        log.info("Updating activity status. activityId->{}, newStatus->{}", activityId, status);

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> {
                    log.error("Activity not found for activityId->{}", activityId);
                    return new RuntimeException("Activity not found");
                });

        activity.setStatus(Enum.valueOf(
                com.pm.activityservice.model.ActivityStatus.class, status));

        log.info("Activity status updated. activityId->{}, status->{}",
                activityId, activity.getStatus());

        return ActivityMapper.toDTO(activity);
    }

    public List<ActivityResponse> getUserActivities(UUID userId) {
        log.info("Fetching activities for userId->{}", userId);

        List<ActivityResponse> responses = activityRepository.findByUserId(userId)
                .stream()
                .map(ActivityMapper::toDTO)
                .toList();

        log.info("Fetched {} activities for userId={}", responses.size(), userId);

        return responses;
    }

    public List<ActivityResponse> getUserActivitiesByDate(UUID userId, LocalDate date) {
        log.info("Fetching activities for userId->{} on date->{}", userId, date);

        List<ActivityResponse> responses = activityRepository
                .findByUserIdAndActivityDate(userId, date)
                .stream()
                .map(ActivityMapper::toDTO)
                .toList();

        log.info("Fetched->{} activities for userId->{} on date->{}",
                responses.size(), userId, date);

        return responses;
    }

    @Transactional
    public void deleteActivitiesForUser(String userId) {
        log.info("Starting deletion of activities for userId->{}", userId);

        UUID uuid = UUID.fromString(userId);
        activityRepository.deleteByUserId(uuid);

        log.info("Successfully deleted activities for userId->{}", userId);
    }
}
