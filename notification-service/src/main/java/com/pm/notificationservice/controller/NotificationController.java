package com.pm.notificationservice.controller;

import com.pm.notificationservice.model.Notification;
import com.pm.notificationservice.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@Tag(name = "Notification")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(
            @PathVariable String userId) {
        return ResponseEntity.ok(service.getUserNotifications(userId));
    }
}

