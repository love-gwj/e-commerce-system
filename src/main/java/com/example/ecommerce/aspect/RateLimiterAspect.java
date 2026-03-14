package com.example.ecommerce.aspect;

import com.example.ecommerce.annotation.RateLimiter;
import com.example.ecommerce.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimiterAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(com.example.ecommerce.annotation.RateLimiter)")
    public void rateLimiterPointcut() {
    }

    @Around("rateLimiterPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);

        if (rateLimiter == null) {
            return joinPoint.proceed();
        }

        // 获取请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }
        HttpServletRequest request = attributes.getRequest();

        // 构建限流 key
        String ip = getIpAddress(request);
        String key = rateLimiter.key();
        if (key.isEmpty()) {
            key = method.getDeclaringClass().getName() + ":" + method.getName();
        }
        key = "rate_limit:" + ip + ":" + key;

        // 获取当前请求次数
        Integer count = (Integer) redisTemplate.opsForValue().get(key);
        if (count == null) {
            count = 0;
        }

        // 判断是否超过限流次数
        if (count >= rateLimiter.count()) {
            log.warn("IP: {} 访问 {} 超过限流次数: {}", ip, key, rateLimiter.count());
            throw new BusinessException(rateLimiter.msg());
        }

        // 增加计数
        redisTemplate.opsForValue().increment(key);
        // 设置过期时间
        if (count == 0) {
            redisTemplate.expire(key, rateLimiter.time(), rateLimiter.timeUnit());
        }

        return joinPoint.proceed();
    }

    /**
     * 获取请求 IP 地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个代理时取第一个 IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
