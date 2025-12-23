package com.pm.notificationservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID userId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String title;
    private String message;

    private boolean read;

    private LocalDateTime createdAt;

    public Notification() {
        this.createdAt = LocalDateTime.now();
        this.read = false;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return read == that.read && Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && type == that.type && Objects.equals(title, that.title) && Objects.equals(message, that.message) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, type, title, message, read, createdAt);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", userId=" + userId +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", read=" + read +
                ", createdAt=" + createdAt +
                '}';
    }
}
