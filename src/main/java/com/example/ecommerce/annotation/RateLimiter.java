package com.example.ecommerce.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    /**
     * 限流 key
     */
    String key() default "";

    /**
     * 限流时间窗口
     */
    int time() default 60;

    /**
     * 限流时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 限流次数
     */
    int count() default 100;

    /**
     * 提示消息
     */
    String msg() default "请求过于频繁，请稍后再试";
}
