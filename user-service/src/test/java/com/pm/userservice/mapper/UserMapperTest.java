package com.pm.userservice.mapper;

import com.pm.userservice.dto.UserRequestDTO;
import com.pm.userservice.dto.UserResponseDTO;
import com.pm.userservice.model.FitnessGoal;
import com.pm.userservice.model.Gender;
import com.pm.userservice.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    @Test
    void toEntityMapsAllFields() {
        UserRequestDTO request = new UserRequestDTO();
        request.setName("Alex");
        request.setEmail("alex@example.com");
        request.setAddress("123 Main St");
        request.setWeight(72.5);
        request.setHeight(1.78);
        request.setGender(Gender.FEMALE);
        request.setDateOfBirth("1992-05-20");
        request.setFitnessGoal(FitnessGoal.LOSE_WEIGHT);
        request.setDailyStepGoal(10000);
        request.setSleepGoalHours(7.5);
        request.setNotificationsEnabled(false);

        User user = UserMapper.toEntity(request);

        assertThat(user.getName()).isEqualTo("Alex");
        assertThat(user.getEmail()).isEqualTo("alex@example.com");
        assertThat(user.getAddress()).isEqualTo("123 Main St");
        assertThat(user.getWeight()).isEqualTo(72.5);
        assertThat(user.getHeight()).isEqualTo(1.78);
        assertThat(user.getGender()).isEqualTo(Gender.FEMALE);
        assertThat(user.getDateOfBirth()).isEqualTo(LocalDate.parse("1992-05-20"));
        assertThat(user.getFitnessGoal()).isEqualTo(FitnessGoal.LOSE_WEIGHT);
        assertThat(user.getDailyStepGoal()).isEqualTo(10000);
        assertThat(user.getSleepGoalHours()).isEqualTo(7.5);
        assertThat(user.getNotificationsEnabled()).isFalse();
    }

    @Test
    void toDtoMapsAuditFieldsWhenPresent() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Alex");
        user.setEmail("alex@example.com");
        user.setAddress("123 Main St");
        user.setWeight(70.0);
        user.setHeight(1.72);
        user.setGender(Gender.MALE);
        user.setDateOfBirth(LocalDate.parse("1990-01-01"));
        user.setFitnessGoal(FitnessGoal.GENERAL_FITNESS);
        user.setDailyStepGoal(8000);
        user.setSleepGoalHours(8.0);
        user.setNotificationsEnabled(true);
        user.setCreatedAt(LocalDateTime.parse("2024-01-01T10:15:30"));
        user.setUpdatedAt(LocalDateTime.parse("2024-01-02T11:15:30"));

        UserResponseDTO dto = UserMapper.toDTO(user);

        assertThat(dto.getId()).isEqualTo(user.getId().toString());
        assertThat(dto.getCreatedAt()).isEqualTo("2024-01-01T10:15:30");
        assertThat(dto.getUpdatedAt()).isEqualTo("2024-01-02T11:15:30");
    }

    @Test
    void toDtoLeavesAuditFieldsNullWhenMissing() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Alex");
        user.setEmail("alex@example.com");
        user.setAddress("123 Main St");
        user.setWeight(70.0);
        user.setHeight(1.72);
        user.setGender(Gender.MALE);
        user.setDateOfBirth(LocalDate.parse("1990-01-01"));
        user.setFitnessGoal(FitnessGoal.GENERAL_FITNESS);
        user.setDailyStepGoal(8000);
        user.setSleepGoalHours(8.0);
        user.setNotificationsEnabled(true);

        UserResponseDTO dto = UserMapper.toDTO(user);

        assertThat(dto.getCreatedAt()).isNull();
        assertThat(dto.getUpdatedAt()).isNull();
    }
}
