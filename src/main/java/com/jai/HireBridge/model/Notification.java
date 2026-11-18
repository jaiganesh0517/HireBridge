package com.jai.HireBridge.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class Notification {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "user_id", nullable = false)
	private Long userId;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NotificationType type;
	
	private String title;
	
	@Column(length = 500)
	private String message;
	
	@Column(name = "application_id")
	private Long applicationId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "email_status", nullable = false )
	private EmailStatus emailStatus = EmailStatus.PENDING;
	
	private int attempts;
	
	@Column(name = "last_error",length = 500)
	private String lastError;
	
	@Column(name = "is_read", nullable = false)
	private boolean read = false;
	
	@Column(name = "createdAt", nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@Column(name = "sent_at")
	private LocalDateTime sentAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
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

	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	public EmailStatus getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(EmailStatus emailStatus) {
		this.emailStatus = emailStatus;
	}

	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	public String getLastError() {
		return lastError;
	}

	public void setLastError(String lastError) {
		this.lastError = lastError;
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

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}
	
	
}
