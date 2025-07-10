/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.aspect;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Component;
import ru.mentee.banking.annotation.Cacheable;

@Aspect
@Component
public class CachingAspect {
    @Autowired private CacheManager cacheManager;

    @Around("@annotation(cacheable)")
    public Object cacheResult(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        Cache cache = cacheManager.getCache(cacheable.cacheName());
        if (cache == null) {
            return joinPoint.proceed();
        }

        String cacheKey = generateCacheKey(joinPoint);
        Cache.ValueWrapper cacheValue = cache.get(cacheKey);

        if (cacheValue != null) {
            return cacheValue.get();
        }

        Object result = joinPoint.proceed();

        if (result != null) {
            if (cache instanceof CaffeineCache caffeineCache) {
                setTtlForCacheValue(caffeineCache, cacheKey, result, cacheable.ttl());
            } else {
                cache.put(cacheKey, result);
            }
        }
        return result;
    }

    private String generateCacheKey(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        return methodName + ":" + Arrays.deepHashCode(args);
    }

    private void setTtlForCacheValue(
            CaffeineCache cache, String key, Object value, long ttlSeconds) {
        cache.getNativeCache()
                .policy()
                .expireVariably()
                .ifPresent(
                        policy -> {
                            policy.put(key, value, ttlSeconds, TimeUnit.SECONDS);
                        });
    }
}
