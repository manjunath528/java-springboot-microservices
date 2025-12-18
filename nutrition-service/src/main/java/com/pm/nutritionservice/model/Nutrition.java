package com.pm.nutritionservice.model;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "nutrition")
public class Nutrition {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    private String mealType; // Breakfast, Lunch, Dinner, Snack

    private Integer calories;
    private Integer protein; // in grams
    private Integer carbs;   // in grams
    private Integer fat;     // in grams

    private LocalDate date;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getProtein() {
        return protein;
    }

    public void setProtein(Integer protein) {
        this.protein = protein;
    }

    public Integer getCarbs() {
        return carbs;
    }

    public void setCarbs(Integer carbs) {
        this.carbs = carbs;
    }

    public Integer getFat() {
        return fat;
    }

    public void setFat(Integer fat) {
        this.fat = fat;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Nutrition nutrition = (Nutrition) o;
        return Objects.equals(id, nutrition.id) && Objects.equals(userId, nutrition.userId) && Objects.equals(mealType, nutrition.mealType) && Objects.equals(calories, nutrition.calories) && Objects.equals(protein, nutrition.protein) && Objects.equals(carbs, nutrition.carbs) && Objects.equals(fat, nutrition.fat) && Objects.equals(date, nutrition.date) && Objects.equals(createdAt, nutrition.createdAt) && Objects.equals(updatedAt, nutrition.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, mealType, calories, protein, carbs, fat, date, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Nutrition{" +
                "id=" + id +
                ", userId=" + userId +
                ", mealType='" + mealType + '\'' +
                ", calories=" + calories +
                ", protein=" + protein +
                ", carbs=" + carbs +
                ", fat=" + fat +
                ", date=" + date +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

