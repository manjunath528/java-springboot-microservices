package com.pm.nutritionservice.controller;

import com.pm.nutritionservice.dto.NutritionRequest;
import com.pm.nutritionservice.dto.NutritionResponse;
import com.pm.nutritionservice.service.NutritionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/nutrition")
public class NutritionController {

    private final NutritionService nutritionService;
    private final Logger log = LoggerFactory.getLogger(NutritionController.class);

    public NutritionController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @PostMapping
    public ResponseEntity<NutritionResponse> createNutrition(@Valid @RequestBody NutritionRequest request) {
        log.info("Received request to create nutrition record for userId->{}", request.getUserId());
        NutritionResponse response = nutritionService.createNutrition(request);
        log.info("Nutrition record created with id->{} for userId->{}", response.getId(), response.getUserId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NutritionResponse>> getUserNutrition(@PathVariable UUID userId) {
        log.info("Fetching nutrition records for userId->{}", userId);
        List<NutritionResponse> responseList = nutritionService.getUserNutrition(userId);
        log.info("Found {} nutrition records for userId->{}", responseList.size(), userId);
        return ResponseEntity.ok(responseList);
    }
}
