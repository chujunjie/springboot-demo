package com.example.springbootdemo.redis.redislimit;


import java.lang.annotation.*;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 21:28 2019/12/10
 * @Modified By
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Limit {

    /**
     * 资源名称
     *
     * @return String
     */
    String name() default "";

    /**
     * 资源key
     *
     * @return String
     */
    String key() default "";

    /**
     * Key前缀
     *
     * @return String
     */
    String prefix() default "";

    /**
     * 给定时间段，单位秒
     *
     * @return int
     */
    int period();

    /**
     * 最多访问限制次数
     *
     * @return int
     */
    int count();

    /**
     * 类型
     *
     * @return LimitType
     */
    LimitType limitType() default LimitType.CUSTOMER;
}

