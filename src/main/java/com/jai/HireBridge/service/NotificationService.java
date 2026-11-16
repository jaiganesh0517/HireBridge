package com.jai.HireBridge.service;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.jai.HireBridge.dto.NotificationResponse;
import com.jai.HireBridge.exception.ResourceNotFoundException;
import com.jai.HireBridge.repositories.NotificationRepository;

import jakarta.transaction.Transactional;

@Service
public class NotificationService {

    private final NotificationRepository repo;

    public NotificationService(NotificationRepository repo) {
        this.repo = repo;
    }

    public List<NotificationResponse> list(Long userId, int page, int size) {
        int safeSize = Math.min(Math.max(size, 1), 50);   // cap so nobody requests 100000 rows
        return repo.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(Math.max(page, 0), safeSize))
                .stream().map(NotificationResponse::from).toList();
    }

    public long unreadCount(Long userId) {
        return repo.countByUserIdAndReadFalse(userId);
    }

    @Transactional
    public void markRead(Long userId, Long notificationId) {
        if (repo.markRead(notificationId, userId) == 0) {
            throw new ResourceNotFoundException("Notification not found");
        }
    }

    @Transactional
    public int markAllRead(Long userId) {
        return repo.markAllRead(userId);
    }
}