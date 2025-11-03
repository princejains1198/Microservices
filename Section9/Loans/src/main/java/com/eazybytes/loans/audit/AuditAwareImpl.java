package com.eazybytes.loans.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    /**
     * This method returns the current auditor's name.
     * In this implementation, it returns a fixed value "Loans_MS".
     *
     * @return An Optional containing the name of the current auditor.
     */
    @Override
    public java.util.Optional<String> getCurrentAuditor() {
        return java.util.Optional.of("Loans_MS");
    }
}
