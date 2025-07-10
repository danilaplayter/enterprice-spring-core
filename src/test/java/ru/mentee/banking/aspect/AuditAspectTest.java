/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.aspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import ru.mentee.banking.annotation.Auditable;
import ru.mentee.banking.api.dto.AuditEntry;
import ru.mentee.banking.api.dto.Status;
import ru.mentee.banking.service.AuditService;

@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "testUser")
class AuditAspectTest {

    @Mock private AuditService auditService;

    @InjectMocks private AuditAspect auditAspect;

    @Mock private ProceedingJoinPoint joinPoint;

    @Mock private SecurityContext securityContext;

    @Mock private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
    }

    @Test
    @DisplayName("Аспект должен корректно обрабатывать успешное выполнение метода")
    void audit_SuccessfulExecution() throws Throwable {
        // Arrange
        Auditable auditable = mock(Auditable.class);
        when(auditable.action()).thenReturn("testAction");

        Object[] args = new Object[] {"param1", 123};
        when(joinPoint.getArgs()).thenReturn(args);

        Object expectedResult = "success";
        when(joinPoint.proceed()).thenReturn(expectedResult);

        // Act
        Object actualResult = auditAspect.audit(joinPoint, auditable);

        // Assert
        assertEquals(expectedResult, actualResult);

        ArgumentCaptor<AuditEntry> entryCaptor = ArgumentCaptor.forClass(AuditEntry.class);
        verify(auditService).save(entryCaptor.capture());

        AuditEntry savedEntry = entryCaptor.getValue();
        assertEquals("testAction", savedEntry.getAction());
        assertEquals("testUser", savedEntry.getUser());
        assertEquals("[param1, 123]", savedEntry.getParameters());
        assertEquals(Status.SUCCESS, savedEntry.getStatus());
        assertEquals("success", savedEntry.getResult());
        assertNotNull(savedEntry.getTimeStamp());
    }

    @Test
    @DisplayName("Аспект должен корректно обрабатывать исключение при выполнении метода")
    void audit_FailedExecution() throws Throwable {
        // Arrange
        Auditable auditable = mock(Auditable.class);
        when(auditable.action()).thenReturn("testAction");

        Object[] args = new Object[] {"param1", 123};
        when(joinPoint.getArgs()).thenReturn(args);

        Exception exception = new RuntimeException("Test error");
        when(joinPoint.proceed()).thenThrow(exception);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> auditAspect.audit(joinPoint, auditable));

        ArgumentCaptor<AuditEntry> entryCaptor = ArgumentCaptor.forClass(AuditEntry.class);
        verify(auditService).save(entryCaptor.capture());

        AuditEntry savedEntry = entryCaptor.getValue();
        assertEquals("testAction", savedEntry.getAction());
        assertEquals("testUser", savedEntry.getUser());
        assertEquals("[param1, 123]", savedEntry.getParameters());
        assertEquals(Status.FAILED, savedEntry.getStatus());
        assertEquals("Test error", savedEntry.getError());
        assertNotNull(savedEntry.getTimeStamp());
    }

    @Test
    @DisplayName("Аспект должен корректно обрабатывать null параметры")
    void audit_NullParameters() throws Throwable {
        // Arrange
        Auditable auditable = mock(Auditable.class);
        when(auditable.action()).thenReturn("testAction");

        when(joinPoint.getArgs()).thenReturn(null);

        Object expectedResult = "result";
        when(joinPoint.proceed()).thenReturn(expectedResult);

        // Act
        Object actualResult = auditAspect.audit(joinPoint, auditable);

        // Assert
        assertEquals(expectedResult, actualResult);

        ArgumentCaptor<AuditEntry> entryCaptor = ArgumentCaptor.forClass(AuditEntry.class);
        verify(auditService).save(entryCaptor.capture());

        AuditEntry savedEntry = entryCaptor.getValue();
        assertEquals("testAction", savedEntry.getAction());
        assertEquals("testUser", savedEntry.getUser());
        assertTrue(savedEntry.getParameters().contains("null"));
        assertEquals(Status.SUCCESS, savedEntry.getStatus());
        assertEquals("result", savedEntry.getResult());
        assertNotNull(savedEntry.getTimeStamp());
    }
}
