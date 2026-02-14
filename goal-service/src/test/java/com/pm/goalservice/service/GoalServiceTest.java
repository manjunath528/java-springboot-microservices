package com.pm.goalservice.service;

import com.pm.goalservice.dto.CreateGoalRequest;
import com.pm.goalservice.kafka.GoalEventProducer;
import com.pm.goalservice.model.Goal;
import com.pm.goalservice.model.GoalStatus;
import com.pm.goalservice.model.GoalType;
import com.pm.goalservice.repository.GoalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

    @Mock
    private GoalRepository repository;

    @Mock
    private GoalEventProducer producer;

    @InjectMocks
    private GoalService goalService;

    @Test
    void createGoalSavesAndPublishesEvent() {
        CreateGoalRequest request = sampleRequest();
        when(repository.findByUserIdAndStatus(request.getUserId(), GoalStatus.ACTIVE))
                .thenReturn(Optional.empty());

        Goal saved = sampleGoal(request.getUserId());
        when(repository.save(any(Goal.class))).thenReturn(saved);

        Goal result = goalService.createGoal(request);

        assertThat(result).isSameAs(saved);
        verify(producer).publishGoalCreated(saved);
    }

    @Test
    void createGoalThrowsWhenActiveGoalExists() {
        CreateGoalRequest request = sampleRequest();
        when(repository.findByUserIdAndStatus(request.getUserId(), GoalStatus.ACTIVE))
                .thenReturn(Optional.of(sampleGoal(request.getUserId())));

        assertThatThrownBy(() -> goalService.createGoal(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Active goal already exists");

        verify(repository, never()).save(any());
        verify(producer, never()).publishGoalCreated(any());
    }

    @Test
    void getActiveGoalReturnsGoalWhenExists() {
        UUID userId = UUID.randomUUID();
        Goal goal = sampleGoal(userId);
        when(repository.findByUserIdAndStatus(userId, GoalStatus.ACTIVE))
                .thenReturn(Optional.of(goal));

        Goal result = goalService.getActiveGoal(userId);

        assertThat(result).isSameAs(goal);
    }

    @Test
    void getActiveGoalThrowsWhenMissing() {
        UUID userId = UUID.randomUUID();
        when(repository.findByUserIdAndStatus(userId, GoalStatus.ACTIVE))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> goalService.getActiveGoal(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No active goal");
    }

    private static CreateGoalRequest sampleRequest() {
        CreateGoalRequest request = new CreateGoalRequest();
        request.setUserId(UUID.randomUUID());
        request.setGoalType(GoalType.ENDURANCE);
        request.setTargetValue(10);
        request.setTargetDate(LocalDate.parse("2024-12-31"));
        return request;
    }

    private static Goal sampleGoal(UUID userId) {
        Goal goal = new Goal();
        goal.setGoalId(UUID.randomUUID());
        goal.setUserId(userId);
        goal.setGoalType(GoalType.ENDURANCE);
        goal.setTargetValue(10);
        goal.setTargetDate(LocalDate.parse("2024-12-31"));
        goal.setStatus(GoalStatus.ACTIVE);
        return goal;
    }
}
