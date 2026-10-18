package com.jai.HireBridge.statemachine;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.jai.HireBridge.model.AppliStatus;

@Component
public class ApplicationStateMachine {

    private final Map<AppliStatus, Set<AppliStatus>> allowedTransition = new EnumMap<>(AppliStatus.class);

    public ApplicationStateMachine() {
        allowedTransition.put(AppliStatus.APPLIED,
                EnumSet.of(AppliStatus.SHORTLISTED, AppliStatus.REJECTED));
        allowedTransition.put(AppliStatus.SHORTLISTED,
                EnumSet.of(AppliStatus.SELECTED, AppliStatus.REJECTED));
        allowedTransition.put(AppliStatus.SELECTED, EnumSet.noneOf(AppliStatus.class));
        allowedTransition.put(AppliStatus.REJECTED, EnumSet.noneOf(AppliStatus.class));
    }

    public boolean isValidTransition(AppliStatus current, AppliStatus target) {
        if (current == null || target == null) {
            return false;
        }
        return allowedNext(current).contains(target);
    }

    public Set<AppliStatus> allowedNext(AppliStatus current) {
        if (current == null) {
            return EnumSet.noneOf(AppliStatus.class);
        }
        Set<AppliStatus> next = allowedTransition.get(current);
        return next == null ? EnumSet.noneOf(AppliStatus.class) : EnumSet.copyOf(next);
    }
}