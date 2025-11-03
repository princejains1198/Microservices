package com.eazybytes.accounts.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    /**
     * This method returns the current auditor's name.
     * In a real-world application, this could be fetched from the security context or session.
     *
     * @return Optional containing the auditor's name.
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("ACCOUNTS_MS");
    }
}
