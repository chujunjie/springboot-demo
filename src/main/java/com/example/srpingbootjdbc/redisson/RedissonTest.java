package com.example.srpingbootjdbc.redisson;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
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
    private static String prefix = "redis://";
    private static String LOCK_01 = "lock_01";
    private static String LOCK_02 = "lock_02";
    private static String MAP_01 = "map_01";
    private static String MAP_02 = "map_02";

    public static void main(String[] args) {

        Config config = new Config();
        config.useSingleServer().setAddress(prefix + "localhost:6379");
        RedissonClient client = Redisson.create(config);

//        client.getMap(MAP_01).put(1, "hello");
//        client.getMap(Map_02).put(1, "world");

        /* 获取不同的锁，可以实现并行操作 */
        new Thread(() -> tryLock(LOCK_01, MAP_01, client), "线程1").start();

        new Thread(() -> tryLock(LOCK_02, MAP_02, client), "线程2").start();
    }

    private static void tryLock(String lockName, String recordId, RedissonClient client) {

        RLock rLock = client.getLock(lockName);

        try {
            boolean b = rLock.tryLock(5, 10, TimeUnit.SECONDS);
            if (b) {
                RMap<Integer, String> map = client.getMap(recordId);
                String value = map.get(1);
                System.out.println(Thread.currentThread().getName() + "获取到锁，值为：" + value);
            } else {
                System.out.println("当前锁被其他线程占用");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
