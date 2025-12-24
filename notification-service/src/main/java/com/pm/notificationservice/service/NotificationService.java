package com.pm.notificationservice.service;

import com.pm.notificationservice.model.Notification;
import com.pm.notificationservice.model.NotificationType;
import com.pm.notificationservice.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(String userId,
                                   NotificationType type,
                                   String title,
                                   String message) {

        Notification notification = new Notification();
        notification.setUserId(UUID.fromString(userId));
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);

        notificationRepository.save(notification);

        log.info("Notification created -> userId->{}, type->{}", userId, type);
    }

    public List<Notification> getUserNotifications(String userId) {
        UUID userIdUUID = UUID.fromString(userId);
        return notificationRepository.findByUserId(userIdUUID);
    }
}
