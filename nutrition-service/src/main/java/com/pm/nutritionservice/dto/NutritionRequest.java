package com.pm.nutritionservice.dto;

import org.antlr.v4.runtime.misc.NotNull;
import org.checkerframework.checker.index.qual.Positive;

import java.time.LocalDate;
import java.util.UUID;

public class NutritionRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private String mealType;

    @Positive
    private Integer calories;

    @Positive
    private Integer protein;

    @Positive
    private Integer carbs;

    @Positive
    private Integer fat;

    @NotNull
    private LocalDate date;

    // Constructors
    public NutritionRequest() {}

    public NutritionRequest(UUID userId, String mealType, Integer calories, Integer protein, Integer carbs, Integer fat, LocalDate date) {
        this.userId = userId;
        this.mealType = mealType;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.date = date;
    }

    // Getters and Setters
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public Integer getCalories() { return calories; }
    public void setCalories(Integer calories) { this.calories = calories; }

    public Integer getProtein() { return protein; }
    public void setProtein(Integer protein) { this.protein = protein; }

    public Integer getCarbs() { return carbs; }
    public void setCarbs(Integer carbs) { this.carbs = carbs; }

    public Integer getFat() { return fat; }
    public void setFat(Integer fat) { this.fat = fat; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
