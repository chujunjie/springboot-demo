package com.example.springbootdemo.redis.redislimit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 21:42 2019/12/10
 * @Modified By
 */
@RestController
public class LimiterController {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    @Limit(key = "test", period = 100, count = 10)
    @GetMapping("/test")
    public int testLimiter() {
        // 意味著 100S 内最多允許訪問10次
        return ATOMIC_INTEGER.incrementAndGet();
    }
}
