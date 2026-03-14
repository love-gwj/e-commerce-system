package com.example.ecommerce.aspect;

import com.example.ecommerce.annotation.Idempotent;
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

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class IdempotentAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(com.example.ecommerce.annotation.Idempotent)")
    public void idempotentPointcut() {
    }

    @Around("idempotentPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Idempotent idempotent = method.getAnnotation(Idempotent.class);

        if (idempotent == null) {
            return joinPoint.proceed();
        }

        // 构建幂等 key
        String key = idempotent.key();
        if (key.isEmpty()) {
            // 使用方法名 + 参数生成 key
            StringBuilder sb = new StringBuilder();
            sb.append(method.getDeclaringClass().getName());
            sb.append(":");
            sb.append(method.getName());
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                for (Object arg : args) {
                    sb.append(":").append(arg.toString());
                }
            }
            key = sb.toString();
        }
        key = "idempotent:" + key;

        // 检查是否存在
        Boolean exist = redisTemplate.hasKey(key);
        if (Boolean.TRUE.equals(exist)) {
            log.warn("检测到重复请求: {}", key);
            throw new BusinessException(idempotent.msg());
        }

        // 设置标记
        redisTemplate.opsForValue().set(key, "1", idempotent.expireTime(), TimeUnit.SECONDS);

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            // 业务异常时删除标记
            redisTemplate.delete(key);
            throw e;
        }
    }
}
