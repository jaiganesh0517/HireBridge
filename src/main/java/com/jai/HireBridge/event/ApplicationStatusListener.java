package com.jai.HireBridge.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.jai.HireBridge.service.EmailService;

@Component
public class ApplicationStatusListener {

    private static final Logger log = LoggerFactory.getLogger(ApplicationStatusListener.class);

    private final EmailService emailService;

    public ApplicationStatusListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async("mailExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onStatusChanged(ApplicationStatusChangedEvent event) {
        try {
            emailService.sendStatusEmail(event);
            log.info("Status email sent to {} for application {}", event.studentEmail(), event.applicationId());
        } catch (Exception e) {
            log.error("Failed to send status email for application {}", event.applicationId(), e);
        }
    }
}