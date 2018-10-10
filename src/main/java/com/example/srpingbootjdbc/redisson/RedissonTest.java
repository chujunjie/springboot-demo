package com.example.srpingbootjdbc.redisson;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;


import java.util.concurrent.TimeUnit;

/**
 * @Description: Redisson是一个使用基于NIO的netty开发的高性能redis客户端，
 * 更加注重分布式场景应用的封装，比如分布式锁、异步流式处理、分布式远程服务、分布式调度服务、队列等等
 * @Author: chujunjie
 * @Date: Create in 23:25 2018/10/10
 * @Modified By
 */
public class RedissonTest {
    public static void main(String[] args) {
        String recordId = "record_id";
        String prefix = "redis://";

        Config config = new Config();
        config.useSingleServer().setAddress(prefix + "localhost:6379");
        RedissonClient client = Redisson.create(config);
        RLock rLock = client.getLock(recordId);
        try {
            boolean b = rLock.tryLock(5, 6, TimeUnit.SECONDS);
            if (b) {
                System.out.println("recordId: " + recordId + "获取到锁");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        client.shutdown();
    }
}
