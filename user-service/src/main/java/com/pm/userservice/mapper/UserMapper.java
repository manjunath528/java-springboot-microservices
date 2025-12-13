package com.pm.userservice.mapper;
import com.pm.userservice.dto.UserRequestDTO;
import com.pm.userservice.dto.UserResponseDTO;
import com.pm.userservice.model.User;

import java.time.format.DateTimeFormatter;

public class UserMapper {

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
  private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  public static User toEntity(UserRequestDTO dto) {
    User user = new User();
    user.setName(dto.getName());
    user.setEmail(dto.getEmail());
    user.setAddress(dto.getAddress());
    user.setWeight(dto.getWeight());
    user.setHeight(dto.getHeight());
    user.setGender(dto.getGender());
    user.setDateOfBirth(java.time.LocalDate.parse(dto.getDateOfBirth(), DATE_FORMAT));
    user.setFitnessGoal(dto.getFitnessGoal());
    user.setDailyStepGoal(dto.getDailyStepGoal());
    user.setSleepGoalHours(dto.getSleepGoalHours());
    user.setNotificationsEnabled(dto.getNotificationsEnabled());
    return user;
  }

  public static UserResponseDTO toDTO(User user) {
    UserResponseDTO dto = new UserResponseDTO();
    dto.setId(user.getId().toString());
    dto.setName(user.getName());
    dto.setEmail(user.getEmail());
    dto.setAddress(user.getAddress());
    dto.setWeight(user.getWeight());
    dto.setHeight(user.getHeight());
    dto.setGender(user.getGender());
    dto.setDateOfBirth(user.getDateOfBirth().format(DATE_FORMAT));
    dto.setFitnessGoal(user.getFitnessGoal());
    dto.setDailyStepGoal(user.getDailyStepGoal());
    dto.setSleepGoalHours(user.getSleepGoalHours());
    dto.setNotificationsEnabled(user.getNotificationsEnabled());

    if (user.getCreatedAt() != null) {
      dto.setCreatedAt(user.getCreatedAt().format(DATE_TIME_FORMAT));
    }
    if (user.getUpdatedAt() != null) {
      dto.setUpdatedAt(user.getUpdatedAt().format(DATE_TIME_FORMAT));
    }

    return dto;
  }
}
