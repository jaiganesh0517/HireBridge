package com.jai.HireBridge.event;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.jai.HireBridge.exception.EmailSendFailedException;
import com.jai.HireBridge.model.EmailStatus;
import com.jai.HireBridge.model.Notification;
import com.jai.HireBridge.model.NotificationType;
import com.jai.HireBridge.repositories.NotificationRepository;
import com.jai.HireBridge.service.EmailService;

@Component
public class ApplicationStatusListener {

    private static final Logger log = LoggerFactory.getLogger(ApplicationStatusListener.class);

    private final EmailService emailService;
    private final NotificationRepository notificationRepo;

    public ApplicationStatusListener(EmailService emailService,
                                     NotificationRepository notificationRepo) {
        this.emailService = emailService;
        this.notificationRepo = notificationRepo;
    }

    @Async("mailExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onStatusChanged(ApplicationStatusChangedEvent event) {
        Notification n = createNotification(event);   // null if the insert itself failed

        try {
            int attempts = emailService.sendStatusEmail(event);
            update(n, EmailStatus.SENT, attempts, null);
            log.info("Status email sent to {} for application {}", event.studentEmail(), event.applicationId());
        } catch (EmailSendFailedException e) {
            update(n, EmailStatus.FAILED, e.getAttempts(), e.getMessage());
            log.error("Failed to send status email for application {}", event.applicationId(), e);
        } catch (Exception e) {
            update(n, EmailStatus.FAILED, 0, e.getMessage());   // e.g. template error, never reached SMTP
            log.error("Failed to prepare status email for application {}", event.applicationId(), e);
        }
    }

    private Notification createNotification(ApplicationStatusChangedEvent event) {
        try {
            Notification n = new Notification();
            n.setUserId(event.studentId());
            n.setType(NotificationType.APPLICATION_STATUS_CHANGED);
            n.setTitle("Application " + event.newStatus());
            n.setMessage("Your application for " + event.jobTitle() + " at "
                    + event.companyName() + " is now " + event.newStatus() + ".");
            n.setApplicationId(event.applicationId());
            return notificationRepo.save(n);
        } catch (Exception e) {
            log.error("Could not save notification for application {}", event.applicationId(), e);
            return null;   // still try to send the email
        }
    }

    private void update(Notification n, EmailStatus status, int attempts, String error) {
        if (n == null) return;
        try {
            n.setEmailStatus(status);
            n.setAttempts(attempts);
            n.setLastError(error == null ? null : error.substring(0, Math.min(error.length(), 500)));
            if (status == EmailStatus.SENT) n.setSentAt(LocalDateTime.now());
            notificationRepo.save(n);
        } catch (Exception e) {
            log.error("Could not update notification {}", n.getId(), e);
        }
    }
}