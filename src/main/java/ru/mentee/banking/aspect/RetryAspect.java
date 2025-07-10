/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.aspect;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.mentee.banking.annotation.Retryable;

@Aspect
@Component
@Slf4j
public class RetryAspect {

    @Around("@annotation(retryable)")
    public Object retry(ProceedingJoinPoint proceedingJoinPoint, Retryable retryable)
            throws Throwable {
        Exception lastEx = null;
        for (int i = 0; i < retryable.maxAttempts(); i++) {
            try {
                return proceedingJoinPoint.proceed();
            } catch (Exception exception) {
                if (Arrays.stream(retryable.retryOn()).anyMatch(e -> e.isInstance(exception))) {
                    lastEx = exception;
                    log.warn("Attempt {} failed. Retrying...", i + 1);
                } else throw exception;
            }
        }
        throw lastEx;
    }
}
