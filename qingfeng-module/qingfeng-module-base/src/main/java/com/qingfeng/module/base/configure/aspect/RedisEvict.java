package com.qingfeng.module.base.configure.aspect;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisEvict {
    String key();

    String fieldKey();
}
