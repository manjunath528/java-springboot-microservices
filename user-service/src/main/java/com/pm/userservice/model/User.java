package com.pm.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
@Entity
@Table(name = "user_table")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String address;

  private Double weight;
  private Double height;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @Enumerated(EnumType.STRING)
  private FitnessGoal fitnessGoal;

  private Integer dailyStepGoal;
  private Double sleepGoalHours;

  private Boolean notificationsEnabled = true;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  private Boolean isActive = true;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
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

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Boolean getActive() {
    return isActive;
  }

  public void setActive(Boolean active) {
    isActive = active;
  }

  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", address='" + address + '\'' +
            ", weight=" + weight +
            ", height=" + height +
            ", gender=" + gender +
            ", dateOfBirth=" + dateOfBirth +
            ", fitnessGoal=" + fitnessGoal +
            ", dailyStepGoal=" + dailyStepGoal +
            ", sleepGoalHours=" + sleepGoalHours +
            ", notificationsEnabled=" + notificationsEnabled +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", isActive=" + isActive +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(address, user.address) && Objects.equals(weight, user.weight) && Objects.equals(height, user.height) && gender == user.gender && Objects.equals(dateOfBirth, user.dateOfBirth) && fitnessGoal == user.fitnessGoal && Objects.equals(dailyStepGoal, user.dailyStepGoal) && Objects.equals(sleepGoalHours, user.sleepGoalHours) && Objects.equals(notificationsEnabled, user.notificationsEnabled) && Objects.equals(createdAt, user.createdAt) && Objects.equals(updatedAt, user.updatedAt) && Objects.equals(isActive, user.isActive);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, email, address, weight, height, gender, dateOfBirth, fitnessGoal, dailyStepGoal, sleepGoalHours, notificationsEnabled, createdAt, updatedAt, isActive);
  }
}
