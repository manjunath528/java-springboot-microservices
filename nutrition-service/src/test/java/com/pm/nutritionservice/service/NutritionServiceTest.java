package com.pm.nutritionservice.service;

import com.pm.nutritionservice.dto.NutritionRequest;
import com.pm.nutritionservice.dto.NutritionResponse;
import com.pm.nutritionservice.kafka.KafkaProducer;
import com.pm.nutritionservice.model.Nutrition;
import com.pm.nutritionservice.repository.NutritionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NutritionServiceTest {

    @Mock
    private NutritionRepository nutritionRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private NutritionService nutritionService;

    @Test
    void createNutritionSavesAndPublishesEvent() {
        NutritionRequest request = sampleRequest();
        Nutrition saved = sampleNutrition();
        when(nutritionRepository.save(any(Nutrition.class))).thenReturn(saved);

        NutritionResponse response = nutritionService.createNutrition(request);

        assertThat(response.getId()).isEqualTo(saved.getId());
        assertThat(response.getUserId()).isEqualTo(saved.getUserId());
        verify(kafkaProducer).sendNutritionCreatedEvent(saved);
    }

    @Test
    void getUserNutritionReturnsMappedResults() {
        UUID userId = UUID.randomUUID();
        when(nutritionRepository.findByUserId(userId))
                .thenReturn(List.of(sampleNutrition(), sampleNutrition()));

        List<NutritionResponse> responses = nutritionService.getUserNutrition(userId);

        assertThat(responses).hasSize(2);
    }

    @Test
    void deleteNutritionForUserDeletesByUserId() {
        UUID userId = UUID.randomUUID();

        nutritionService.deleteNutritionForUser(userId.toString());

        verify(nutritionRepository).deleteByUserId(userId);
    }

    @Test
    void deleteNutritionForUserThrowsOnInvalidUuid() {
        assertThatThrownBy(() -> nutritionService.deleteNutritionForUser("not-a-uuid"))
                .isInstanceOf(IllegalArgumentException.class);
        verify(nutritionRepository, never()).deleteByUserId(any());
    }

    private static NutritionRequest sampleRequest() {
        NutritionRequest request = new NutritionRequest();
        request.setUserId(UUID.randomUUID());
        request.setMealType("LUNCH");
        request.setCalories(600);
        request.setProtein(30);
        request.setCarbs(70);
        request.setFat(20);
        request.setDate(LocalDate.now());
        return request;
    }

    private static Nutrition sampleNutrition() {
        Nutrition nutrition = new Nutrition();
        nutrition.setId(UUID.randomUUID());
        nutrition.setUserId(UUID.randomUUID());
        nutrition.setMealType("DINNER");
        nutrition.setCalories(800);
        nutrition.setProtein(40);
        nutrition.setCarbs(90);
        nutrition.setFat(25);
        nutrition.setDate(LocalDate.now());
        nutrition.setCreatedAt(LocalDateTime.now());
        nutrition.setUpdatedAt(LocalDateTime.now());
        return nutrition;
    }
}
