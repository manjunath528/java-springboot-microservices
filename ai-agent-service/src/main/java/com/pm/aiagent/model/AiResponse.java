package com.pm.aiagent.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class AiResponse {
    private String userId;
    private String message;
    private String recommendationType; // WORKOUT, NUTRITION, GENERAL
    private LocalDateTime timestamp;

    public AiResponse(String userId, String message, String recommendationType) {
        this.userId = userId;
        this.message = message;
        this.recommendationType = recommendationType;
        this.timestamp = LocalDateTime.now();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecommendationType() {
        return recommendationType;
    }

    public void setRecommendationType(String recommendationType) {
        this.recommendationType = recommendationType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AiResponse that = (AiResponse) o;
        return Objects.equals(userId, that.userId) && Objects.equals(message, that.message) && Objects.equals(recommendationType, that.recommendationType) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, message, recommendationType, timestamp);
    }

    @Override
    public String toString() {
        return "AiResponse{" +
                "userId='" + userId + '\'' +
                ", message='" + message + '\'' +
                ", recommendationType='" + recommendationType + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
