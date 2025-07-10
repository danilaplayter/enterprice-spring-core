/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.aspect;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeoutException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import ru.mentee.banking.annotation.Retryable;

@ExtendWith(MockitoExtension.class)
class RetryAspectTest {

    @Mock private ProceedingJoinPoint joinPoint;

    @Mock private Logger log;

    private RetryAspect retryAspect;

    @BeforeEach
    void setUp() {
        retryAspect = new RetryAspect();
    }

    // Happy path tests
    @Test
    void retry_ShouldSucceedOnFirstAttempt() throws Throwable {
        Retryable retryable = mockRetryable(3, RuntimeException.class);
        when(joinPoint.proceed()).thenReturn("success");

        Object result = retryAspect.retry(joinPoint, retryable);

        assertEquals("success", result);
        verify(joinPoint, times(1)).proceed();
        verifyNoInteractions(log);
    }

    @Test
    void retry_ShouldThrowImmediatelyForNonRetryableException() throws Throwable {
        Retryable retryable = mockRetryable(3, IllegalArgumentException.class);
        when(joinPoint.proceed()).thenThrow(new NullPointerException("Wrong type"));

        assertThrows(NullPointerException.class, () -> retryAspect.retry(joinPoint, retryable));

        verify(joinPoint, times(1)).proceed();
        verifyNoInteractions(log);
    }

    // Edge cases
    @Test
    void retry_ShouldWorkWithSingleAttempt() throws Throwable {
        Retryable retryable = mockRetryable(1, Exception.class);
        when(joinPoint.proceed()).thenThrow(new Exception("Fail"));

        assertThrows(Exception.class, () -> retryAspect.retry(joinPoint, retryable));

        verify(joinPoint, times(1)).proceed();
    }

    @Test
    void retry_ShouldHandleMultipleExceptionTypes() throws Throwable {
        Retryable retryable = mockRetryable(3, IOException.class, TimeoutException.class);
        when(joinPoint.proceed())
                .thenThrow(new IOException("IO fail"))
                .thenThrow(new TimeoutException("Timeout"))
                .thenReturn("success");

        assertEquals("success", retryAspect.retry(joinPoint, retryable));
        verify(joinPoint, times(3)).proceed();
    }

    @Test
    void retry_ShouldHandleEmptyRetryOnList() throws Throwable {
        Retryable retryable = mockRetryable(3); // No exception types
        when(joinPoint.proceed()).thenThrow(new Exception("Any exception"));

        assertThrows(Exception.class, () -> retryAspect.retry(joinPoint, retryable));

        verify(joinPoint, times(1)).proceed();
    }

    // Helper method
    private Retryable mockRetryable(int maxAttempts, Class<? extends Exception>... retryOn) {
        return new Retryable() {
            @Override
            public Class<? extends Exception>[] retryOn() {
                return retryOn;
            }

            @Override
            public int maxAttempts() {
                return maxAttempts;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Retryable.class;
            }
        };
    }
}
