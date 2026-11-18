package com.jai.HireBridge.dto;

import java.time.LocalDateTime;

import com.jai.HireBridge.model.Notification;

public class NotificationResponse {

    private Long id;
    private String title;
    private String message;
    private Long applicationId;
    private boolean read;
    private LocalDateTime createdAt;

    public NotificationResponse() {}

    public NotificationResponse(Long id, String title, String message,
                                Long applicationId, boolean read, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.applicationId = applicationId;
        this.read = read;
        this.createdAt = createdAt;
    }

    public static NotificationResponse from(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getTitle(),
                n.getMessage(),
                n.getApplicationId(),
                n.isRead(),
                n.getCreatedAt());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}