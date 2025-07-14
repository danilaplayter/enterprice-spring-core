/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.aspect;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.mentee.banking.annotation.Auditable;
import ru.mentee.banking.api.dto.AuditEntry;
import ru.mentee.banking.api.dto.Status;

@Component
public class AuditEntryFactory {
    public AuditEntry createEntry(ProceedingJoinPoint joinPoint, Auditable auditable) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        Object[] args = joinPoint.getArgs();

        AuditEntry entry = new AuditEntry();
        entry.setAction(auditable.action());
        entry.setUser(user);
        entry.setTimeStamp(LocalDateTime.now());
        entry.setParameters(Arrays.toString(args));

        return entry;
    }

    public void populateSuccess(AuditEntry entry, Object result) {
        entry.setStatus(Status.SUCCESS);
        entry.setResult(String.valueOf(result));
    }

    public void populateFailure(AuditEntry entry, Exception exception) {
        entry.setStatus(Status.FAILED);
        entry.setError(exception.getMessage());
    }
}
