package com.pm.nutritionservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.nutritionservice.dto.NutritionRequest;
import com.pm.nutritionservice.dto.NutritionResponse;
import com.pm.nutritionservice.service.NutritionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NutritionController.class)
class NutritionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NutritionService nutritionService;

    @Test
    void createNutritionReturnsCreated() throws Exception {
        NutritionRequest request = sampleRequest();
        NutritionResponse response = sampleResponse();
        when(nutritionService.createNutrition(any(NutritionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/nutrition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.getId().toString()));
    }

    @Test
    void getUserNutritionReturnsOk() throws Exception {
        UUID userId = UUID.randomUUID();
        NutritionResponse response = sampleResponse();
        when(nutritionService.getUserNutrition(userId)).thenReturn(List.of(response));

        mockMvc.perform(get("/nutrition/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.getId().toString()));
    }

    private static NutritionRequest sampleRequest() {
        NutritionRequest request = new NutritionRequest();
        request.setUserId(UUID.randomUUID());
        request.setMealType("LUNCH");
        request.setCalories(600);
        request.setProtein(30);
        request.setCarbs(70);
        request.setFat(20);
        request.setDate(LocalDate.parse("2024-01-10"));
        return request;
    }

    private static NutritionResponse sampleResponse() {
        NutritionResponse response = new NutritionResponse();
        response.setId(UUID.randomUUID());
        response.setUserId(UUID.randomUUID());
        response.setMealType("LUNCH");
        response.setCalories(600);
        response.setProtein(30);
        response.setCarbs(70);
        response.setFat(20);
        response.setDate(LocalDate.parse("2024-01-10"));
        return response;
    }
}
