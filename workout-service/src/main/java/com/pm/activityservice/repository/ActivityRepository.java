package com.pm.activityservice.repository;

import com.pm.activityservice.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    List<Activity> findByUserId(UUID userId);

    List<Activity> findByUserIdAndActivityDate(UUID userId, LocalDate activityDate);
}
