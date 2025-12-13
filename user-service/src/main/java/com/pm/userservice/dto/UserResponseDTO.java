package com.pm.userservice.dto;
import com.pm.userservice.model.FitnessGoal;
import com.pm.userservice.model.Gender;

public class UserResponseDTO {

  private String id;
  private String name;
  private String email;
  private String address;
  private Double weight;
  private Double height;
  private Gender gender;
  private String dateOfBirth;
  private FitnessGoal fitnessGoal;
  private Integer dailyStepGoal;
  private Double sleepGoalHours;
  private Boolean notificationsEnabled;

  // Optional audit fields
  private String createdAt;
  private String updatedAt;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

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

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }
}
