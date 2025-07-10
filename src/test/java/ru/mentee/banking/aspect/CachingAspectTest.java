/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.aspect;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import ru.mentee.banking.annotation.Cacheable;

@ExtendWith(MockitoExtension.class)
class CachingAspectTest {

    @Mock(lenient = true)
    private ProceedingJoinPoint joinPoint;

    @Mock(lenient = true)
    private Signature signature;

    @Mock(lenient = true)
    private Cacheable cacheableAnnotation;

    @Mock private CacheManager cacheManager;

    @InjectMocks private CachingAspect cachingAspect;

    @Captor private ArgumentCaptor<String> keyCaptor;

    @Captor private ArgumentCaptor<Object> valueCaptor;

    private final String cacheName = "testCache";
    private final String methodName = "testMethod";
    private final Object[] methodArgs = new Object[] {"arg1", 42};
    private final Object cachedValue = "cachedValue";
    private final Object newValue = "newValue";

    @BeforeEach
    void setUp() {
        // Ленивые заглушки для основных объектов
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn(methodName);
        when(joinPoint.getArgs()).thenReturn(methodArgs);
        when(cacheableAnnotation.cacheName()).thenReturn(cacheName);
    }

    @Test
    void whenCacheNotFound_thenProceedOriginalMethod() throws Throwable {
        when(cacheManager.getCache(cacheName)).thenReturn(null);
        when(joinPoint.proceed()).thenReturn(newValue);

        Object result = cachingAspect.cacheResult(joinPoint, cacheableAnnotation);

        verify(joinPoint).proceed();
        assertEquals(newValue, result);
    }

    @Test
    void whenValueInCache_thenReturnCachedValue() throws Throwable {
        org.springframework.cache.Cache cache = mock(org.springframework.cache.Cache.class);
        when(cacheManager.getCache(cacheName)).thenReturn(cache);
        when(cache.get(generateExpectedKey())).thenReturn(() -> cachedValue);

        Object result = cachingAspect.cacheResult(joinPoint, cacheableAnnotation);

        verify(joinPoint, never()).proceed();
        assertEquals(cachedValue, result);
    }

    @Test
    void whenValueNotInCache_thenProceedAndCacheResult() throws Throwable {
        org.springframework.cache.Cache cache = mock(org.springframework.cache.Cache.class);
        when(cacheManager.getCache(cacheName)).thenReturn(cache);
        when(cache.get(generateExpectedKey())).thenReturn(null);
        when(joinPoint.proceed()).thenReturn(newValue);

        Object result = cachingAspect.cacheResult(joinPoint, cacheableAnnotation);

        verify(cache).put(keyCaptor.capture(), valueCaptor.capture());
        assertEquals(generateExpectedKey(), keyCaptor.getValue());
        assertEquals(newValue, valueCaptor.getValue());
        assertEquals(newValue, result);
    }

    @Test
    void whenResultIsNull_thenDoNotCache() throws Throwable {
        org.springframework.cache.Cache cache = mock(org.springframework.cache.Cache.class);
        when(cacheManager.getCache(cacheName)).thenReturn(cache);
        when(cache.get(anyString())).thenReturn(null);
        when(joinPoint.proceed()).thenReturn(null);

        Object result = cachingAspect.cacheResult(joinPoint, cacheableAnnotation);

        verify(cache, never()).put(anyString(), any());
        assertNull(result);
    }

    private String generateExpectedKey() {
        return methodName + ":" + Arrays.deepHashCode(methodArgs);
    }
}
