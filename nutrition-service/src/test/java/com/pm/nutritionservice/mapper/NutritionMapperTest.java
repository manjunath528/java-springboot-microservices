package com.pm.nutritionservice.mapper;

import com.pm.nutritionservice.dto.NutritionRequest;
import com.pm.nutritionservice.dto.NutritionResponse;
import com.pm.nutritionservice.model.Nutrition;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class NutritionMapperTest {

    @Test
    void toEntityMapsAllFields() {
        NutritionRequest request = new NutritionRequest();
        request.setUserId(UUID.randomUUID());
        request.setMealType("BREAKFAST");
        request.setCalories(450);
        request.setProtein(20);
        request.setCarbs(55);
        request.setFat(15);
        request.setDate(LocalDate.parse("2024-01-10"));

        Nutrition nutrition = NutritionMapper.toEntity(request);

        assertThat(nutrition.getUserId()).isEqualTo(request.getUserId());
        assertThat(nutrition.getMealType()).isEqualTo("BREAKFAST");
        assertThat(nutrition.getCalories()).isEqualTo(450);
        assertThat(nutrition.getProtein()).isEqualTo(20);
        assertThat(nutrition.getCarbs()).isEqualTo(55);
        assertThat(nutrition.getFat()).isEqualTo(15);
        assertThat(nutrition.getDate()).isEqualTo(LocalDate.parse("2024-01-10"));
    }

    @Test
    void toDtoMapsAllFields() {
        Nutrition nutrition = new Nutrition();
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        nutrition.setId(id);
        nutrition.setUserId(userId);
        nutrition.setMealType("SNACK");
        nutrition.setCalories(300);
        nutrition.setProtein(10);
        nutrition.setCarbs(40);
        nutrition.setFat(12);
        nutrition.setDate(LocalDate.parse("2024-01-11"));
        nutrition.setCreatedAt(LocalDateTime.parse("2024-01-11T10:15:30"));
        nutrition.setUpdatedAt(LocalDateTime.parse("2024-01-12T11:15:30"));

        NutritionResponse response = NutritionMapper.toDTO(nutrition);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getMealType()).isEqualTo("SNACK");
        assertThat(response.getCalories()).isEqualTo(300);
        assertThat(response.getProtein()).isEqualTo(10);
        assertThat(response.getCarbs()).isEqualTo(40);
        assertThat(response.getFat()).isEqualTo(12);
        assertThat(response.getDate()).isEqualTo(LocalDate.parse("2024-01-11"));
        assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.parse("2024-01-11T10:15:30"));
        assertThat(response.getUpdatedAt()).isEqualTo(LocalDateTime.parse("2024-01-12T11:15:30"));
    }
}
