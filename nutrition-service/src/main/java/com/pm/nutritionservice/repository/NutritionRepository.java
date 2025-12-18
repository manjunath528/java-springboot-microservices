package com.pm.nutritionservice.repository;

import com.pm.nutritionservice.model.Nutrition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface NutritionRepository extends JpaRepository<Nutrition, UUID> {
    List<Nutrition> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);
}
