/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.aspect;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.mentee.banking.annotation.RequiresRole;

@Aspect
@Component
public class SecurityAspect {
    private static final Logger logger = LoggerFactory.getLogger(SecurityAspect.class);

    @Around("@annotation(requiresRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RequiresRole requiresRole)
            throws Throwable {
        try {
            logger.debug("Начало проверки ролей для метода: {}");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                logger.warn(
                        "Попытка доступа неаутентифицированного пользователя к защищенному методу:"
                                + " {}",
                        joinPoint.getSignature().getName());
                throw new SecurityException("Доступ запрещен. Пользователь не аутентифицирован.");
            }

            Collection<String> userRoles =
                    authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toSet());

            String[] requiredRoles = requiresRole.value();
            logger.debug(
                    "Требуемые роли: {}, Роли пользователя: {}",
                    Arrays.toString(requiredRoles),
                    userRoles);

            boolean hasRequiredRole = Arrays.stream(requiredRoles).anyMatch(userRoles::contains);
            if (!hasRequiredRole) {
                logger.warn(
                        "Отказано в доступе пользователю {} (роли: {}) к методу {}, требуемые роли:"
                                + " {}",
                        authentication.getName(),
                        userRoles,
                        joinPoint.getSignature().getName(),
                        Arrays.toString(requiredRoles));
            }
            logger.debug("Доступ разрешен для пользователя {}", authentication.getName());
            return joinPoint.proceed();

        } catch (SecurityException exception) {
            throw exception;
        } catch (Exception exception) {
            logger.error("Ошибка при проверке прав доступа: ", exception);
            throw new SecurityException("Произошла ошибка при проверке прав доступа", exception);
        }
    }
}
