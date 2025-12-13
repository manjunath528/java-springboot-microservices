package com.pm.userservice.dto;

import com.pm.userservice.dto.validators.CreateUserValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.pm.userservice.model.FitnessGoal;
import com.pm.userservice.model.Gender;
import jakarta.validation.constraints.*;

public class UserRequestDTO {

  @NotBlank
  @Size(max = 100)
  private String name;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String address;

  @NotNull
  private Double weight;

  @NotNull
  private Double height;

  @NotNull
  private Gender gender;

  @NotBlank
  private String dateOfBirth; // yyyy-MM-dd

  private FitnessGoal fitnessGoal = FitnessGoal.GENERAL_FITNESS;

  private Integer dailyStepGoal;

  private Double sleepGoalHours;

  private Boolean notificationsEnabled = true;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Double getWeight() {
    return weight;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public Double getHeight() {
    return height;
  }

  public void setHeight(Double height) {
    this.height = height;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public FitnessGoal getFitnessGoal() {
    return fitnessGoal;
  }

  public void setFitnessGoal(FitnessGoal fitnessGoal) {
    this.fitnessGoal = fitnessGoal;
  }

  public Integer getDailyStepGoal() {
    return dailyStepGoal;
  }

  public void setDailyStepGoal(Integer dailyStepGoal) {
    this.dailyStepGoal = dailyStepGoal;
  }

  public Double getSleepGoalHours() {
    return sleepGoalHours;
  }

  public void setSleepGoalHours(Double sleepGoalHours) {
    this.sleepGoalHours = sleepGoalHours;
  }

  public Boolean getNotificationsEnabled() {
    return notificationsEnabled;
  }

  public void setNotificationsEnabled(Boolean notificationsEnabled) {
    this.notificationsEnabled = notificationsEnabled;
  }
}
