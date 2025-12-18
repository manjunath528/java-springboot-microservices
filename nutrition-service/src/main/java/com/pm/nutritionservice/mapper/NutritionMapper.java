package com.pm.nutritionservice.mapper;

import com.pm.nutritionservice.dto.NutritionRequest;
import com.pm.nutritionservice.dto.NutritionResponse;
import com.pm.nutritionservice.model.Nutrition;

public class NutritionMapper {

    public static Nutrition toEntity(NutritionRequest request) {
        Nutrition nutrition = new Nutrition();
        nutrition.setUserId(request.getUserId());
        nutrition.setMealType(request.getMealType());
        nutrition.setCalories(request.getCalories());
        nutrition.setProtein(request.getProtein());
        nutrition.setCarbs(request.getCarbs());
        nutrition.setFat(request.getFat());
        nutrition.setDate(request.getDate());
        return nutrition;
    }

    public static NutritionResponse toDTO(Nutrition nutrition) {
        return new NutritionResponse(
                nutrition.getId(),
                nutrition.getUserId(),
                nutrition.getMealType(),
                nutrition.getCalories(),
                nutrition.getProtein(),
                nutrition.getCarbs(),
                nutrition.getFat(),
                nutrition.getDate(),
                nutrition.getCreatedAt(),
                nutrition.getUpdatedAt()
        );
    }
}
