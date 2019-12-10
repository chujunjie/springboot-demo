package com.example.springbootdemo.redis.redislimit;

public enum LimitType {
    /**
     * 自定义key
     */
    CUSTOMER,

    /**
     * 根据请求者IP
     */
    IP
}
