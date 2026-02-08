package com.pm.notificationservice.controller;

import com.pm.notificationservice.model.Notification;
import com.pm.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    void getUserNotificationsReturnsOk() throws Exception {
        UUID userId = UUID.randomUUID();
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID());
        when(notificationService.getUserNotifications(userId.toString()))
                .thenReturn(List.of(notification));

        mockMvc.perform(get("/notifications/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(notification.getId().toString()));
    }
}
