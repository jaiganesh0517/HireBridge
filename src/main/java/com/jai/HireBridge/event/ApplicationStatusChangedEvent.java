package com.jai.HireBridge.event;

import com.jai.HireBridge.model.AppliStatus;

public record ApplicationStatusChangedEvent(
        Long applicationId,
        Long studentId,
        String studentName,
        String studentEmail,
        String jobTitle,
        String companyName,
        AppliStatus oldStatus,
        AppliStatus newStatus) {}