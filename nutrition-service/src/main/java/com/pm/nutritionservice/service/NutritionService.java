package com.pm.nutritionservice.service;
import com.pm.nutritionservice.dto.NutritionRequest;
import com.pm.nutritionservice.dto.NutritionResponse;
import com.pm.nutritionservice.kafka.KafkaProducer;
import com.pm.nutritionservice.mapper.NutritionMapper;
import com.pm.nutritionservice.model.Nutrition;
import com.pm.nutritionservice.repository.NutritionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NutritionService {

    private final NutritionRepository nutritionRepository;
    private final KafkaProducer kafkaProducer;
    private final Logger log = LoggerFactory.getLogger(NutritionService.class);

    public NutritionService(NutritionRepository nutritionRepository, KafkaProducer kafkaProducer) {
        this.nutritionRepository = nutritionRepository;
        this.kafkaProducer = kafkaProducer;
    }

    public NutritionResponse createNutrition(NutritionRequest request) {
        log.info("Creating nutrition for userId->{}", request.getUserId());
        Nutrition nutrition = NutritionMapper.toEntity(request);
        Nutrition saved = nutritionRepository.save(nutrition);
        kafkaProducer.sendNutritionCreatedEvent(saved);
        return NutritionMapper.toDTO(saved);
    }

    public List<NutritionResponse> getUserNutrition(UUID userId) {
        log.info("Fetching nutrition entries for userId->{}", userId);
        return nutritionRepository.findByUserId(userId)
                .stream()
                .map(NutritionMapper::toDTO)
                .toList();
    }

    @Transactional
    public void deleteNutritionForUser(String userId) {
        log.info("Deleting all nutrition entries for userId->{}", userId);
        nutritionRepository.deleteByUserId(UUID.fromString(userId));
    }
}
