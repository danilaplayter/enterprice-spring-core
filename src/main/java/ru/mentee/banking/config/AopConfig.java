/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;
import ru.mentee.banking.aspect.AuditAspect;
import ru.mentee.banking.aspect.CachingAspect;
import ru.mentee.banking.aspect.RetryAspect;
import ru.mentee.banking.aspect.SecurityAspect;

@Configuration
@EnableAspectJAutoProxy
public class AopConfig {

    @Bean
    @Order(1)
    public SecurityAspect securityAspect() {
        return new SecurityAspect();
    }

    @Bean
    @Order(2)
    public RetryAspect retryAspect() {
        return new RetryAspect();
    }

    @Bean
    @Order(3)
    public CachingAspect cachingAspect() {
        return new CachingAspect();
    }

    @Bean
    @Order(4)
    public AuditAspect auditAspect() {
        return new AuditAspect();
    }
}
