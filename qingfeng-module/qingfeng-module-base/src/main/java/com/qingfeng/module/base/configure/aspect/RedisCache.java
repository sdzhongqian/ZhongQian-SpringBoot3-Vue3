package com.qingfeng.module.base.configure.aspect;

import java.lang.annotation.*;


@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCache {
    /**
     * 键名
     *
     * @return
     */
    String key() default "";

    /**
     * 主键
     *
     * @return
     * @author zmr
     */
    String fieldKey();

    /**
     * 过期时间(秒)
     *
     * @return
     */
    long expired() default 30;


    /**
     * 过期类型，
     * 默认为 0，取expired
     * 等于1是随机时间，expired=expired+Math.random()*expired，防止同一时间大量数据过期现象发生
     * @return
     */
    int expiredType() default 0;

    /**
     * 是否为查询操作 如果为写入数据库的操作，该值需置为 false
     *
     * @return
     */
    boolean read() default true;
}
