package com.jai.HireBridge.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ApplicationStatusListener {

    private static final Logger log = LoggerFactory.getLogger(ApplicationStatusListener.class);

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onStatusChanged(ApplicationStatusChangedEvent event) {
        try {
            log.info("Would email {}: your application for {} at {} is now {}",
                    event.studentEmail(), event.jobTitle(),
                    event.companyName(), event.newStatus());
        } catch (Exception e) {
            log.error("Failed to handle status event for application {}", event.applicationId(), e);
        }
    }
}