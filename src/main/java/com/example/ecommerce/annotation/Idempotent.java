package com.example.ecommerce.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

    /**
     * 幂等 key
     */
    String key() default "";

    /**
     * 过期时间（秒）
     */
    int expireTime() default 10;

    /**
     * 提示消息
     */
    String msg() default "重复请求，请稍后再试";
}
