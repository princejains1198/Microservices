package com.eazybytes.cards.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    /**
     * This method returns the current auditor's name.
     * In this implementation, it returns a fixed value "Cards_MS".
     *
     * @return An Optional containing the name of the current auditor.
     */
    @Override
    public Optional getCurrentAuditor() {
        return Optional.of("Cards_MS");
    }
}

