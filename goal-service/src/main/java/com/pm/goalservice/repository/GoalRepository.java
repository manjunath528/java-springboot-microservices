package com.pm.goalservice.repository;

import com.pm.goalservice.model.Goal;
import com.pm.goalservice.model.GoalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoalRepository extends JpaRepository<Goal, UUID> {
    Optional<Goal> findByUserIdAndStatus(UUID userId, GoalStatus status);
}

