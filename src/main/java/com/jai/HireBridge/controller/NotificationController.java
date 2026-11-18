package com.jai.HireBridge.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jai.HireBridge.dto.NotificationResponse;
import com.jai.HireBridge.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping
    public List<NotificationResponse> list(Authentication auth,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return service.list((Long) auth.getPrincipal(), page, size);
    }

    @GetMapping("/unread-count")
    public Map<String, Long> unreadCount(Authentication auth) {
        return Map.of("count", service.unreadCount((Long) auth.getPrincipal()));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markRead(Authentication auth, @PathVariable Long id) {
        service.markRead((Long) auth.getPrincipal(), id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/read-all")
    public Map<String, Integer> markAllRead(Authentication auth) {
        return Map.of("updated", service.markAllRead((Long) auth.getPrincipal()));
    }
}
