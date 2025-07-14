/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.aspect;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.mentee.banking.annotation.Auditable;
import ru.mentee.banking.api.dto.AuditEntry;
import ru.mentee.banking.api.dto.Status;
import ru.mentee.banking.service.AuditService;

@Aspect
@Component
public class AuditAspect {
    private final AuditService auditService;
    private final AuditEntryFactory auditEntryFactory;

    @Autowired
    public AuditAspect(AuditService auditService, AuditEntryFactory auditEntryFactory) {
        this.auditService = auditService;
        this.auditEntryFactory = auditEntryFactory;
    }

    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        String action = auditable.action();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        Object[] args = joinPoint.getArgs();
        AuditEntry entry = new AuditEntry();
        entry.setAction(action);
        entry.setUser(user);
        entry.setTimeStamp(LocalDateTime.now());
        entry.setParameters(Arrays.toString(args));

        try {
            Object result = joinPoint.proceed();
            entry.setStatus(Status.SUCCESS);
            entry.setResult(String.valueOf(result));
            return result;
        } catch (Exception exception) {
            entry.setStatus(Status.FAILED);
            entry.setError(exception.getMessage());
            throw exception;
        } finally {
            auditService.save(entry);
        }
    }
}
