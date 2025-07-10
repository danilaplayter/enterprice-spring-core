/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.mentee.banking.api.dto.AuditEntry;

@Service
@RequiredArgsConstructor
public class AuditService {
    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

    public void save(AuditEntry entry) {
        logger.info("Audit entry: {}", entry);
    }
}
