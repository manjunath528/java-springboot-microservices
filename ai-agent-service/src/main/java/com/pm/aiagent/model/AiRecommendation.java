package com.pm.aiagent.model;

import java.time.Instant;

public record AiRecommendation(
        String userId,
        String sourceType,   // ACTIVITY / NUTRITION / USER
        String message,      // recommendation text
        String severity,     // INFO / WARNING / ACTION
        Instant createdAt
) {}
