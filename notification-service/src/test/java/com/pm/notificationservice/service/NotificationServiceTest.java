package com.pm.notificationservice.service;

import com.pm.notificationservice.model.Notification;
import com.pm.notificationservice.model.NotificationType;
import com.pm.notificationservice.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void createNotificationSavesNotification() {
        String userId = UUID.randomUUID().toString();

        notificationService.createNotification(userId, NotificationType.USER_CREATED,
                "Welcome", "Hello there");

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository).save(captor.capture());

        Notification saved = captor.getValue();
        assertThat(saved.getUserId().toString()).isEqualTo(userId);
        assertThat(saved.getType()).isEqualTo(NotificationType.USER_CREATED);
        assertThat(saved.getTitle()).isEqualTo("Welcome");
        assertThat(saved.getMessage()).isEqualTo("Hello there");
        assertThat(saved.isRead()).isFalse();
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void createNotificationThrowsOnInvalidUserId() {
        assertThatThrownBy(() -> notificationService.createNotification(
                "not-a-uuid", NotificationType.USER_CREATED, "Welcome", "Hello"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getUserNotificationsReturnsRepositoryResults() {
        UUID userId = UUID.randomUUID();
        when(notificationRepository.findByUserId(userId))
                .thenReturn(List.of(new Notification(), new Notification()));

        List<Notification> result = notificationService.getUserNotifications(userId.toString());

        assertThat(result).hasSize(2);
    }

    @Test
    void getUserNotificationsThrowsOnInvalidUserId() {
        assertThatThrownBy(() -> notificationService.getUserNotifications("bad-id"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
