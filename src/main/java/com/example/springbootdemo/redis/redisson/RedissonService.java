package com.example.springbootdemo.redis.redisson;

import org.redisson.api.*;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Description: Redisson操作类
 * @Author: chujunjie
 * @Date: Create in 22:59 2018/10/10
 * @Modified By
 */
@Service("redissonService")
public class RedissonService {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * getRedissonClient
     *
     * @throws IOException IOException
     */
    public void getRedissonClient() throws IOException {
        Config config = redissonClient.getConfig();
        System.out.println(config.toJSON());
    }

    /**
     * 获取字符串对象
     *
     * @param objectName objectName
     * @return RBucket
     */
    public <T> RBucket<T> getRBucket(String objectName) {
        return redissonClient.getBucket(objectName);
    }

    /**
     * 放入Map对象
     *
     * @param objectName objectName
     * @return RMap
     */
    public <K, V> RMap<K, V> putRMap(String objectName) {
        return redissonClient.getMap(objectName);
    }

    /**
     * 获取Map对象
     *
     * @param objectName objectName
     * @return RMap
     */
    public <K, V> RMap<K, V> getRMap(String objectName) {
        return redissonClient.getMap(objectName);
    }

    /**
     * 获取有序集合
     *
     * @param objectName objectName
     * @return RSortedSet
     */
    public <V> RSortedSet<V> getRSortedSet(String objectName) {
        return redissonClient.getSortedSet(objectName);
    }

    /**
     * 获取集合
     *
     * @param objectName objectName
     * @return RSet
     */
    public <V> RSet<V> getRSet(String objectName) {
        return redissonClient.getSet(objectName);
    }

    /**
     * 获取列表
     *
     * @param objectName objectName
     * @return RList
     */
    public <V> RList<V> getRList(String objectName) {
        return redissonClient.getList(objectName);
    }

    /**
     * 获取队列
     *
     * @param objectName objectName
     * @return RQueue
     */
    public <V> RQueue<V> getRQueue(String objectName) {
        return redissonClient.getQueue(objectName);
    }

    /**
     * 获取双端队列
     *
     * @param objectName objectName
     * @return RDeque
     */
    public <V> RDeque<V> getRDeque(String objectName) {
        return redissonClient.getDeque(objectName);
    }


    /**
     * 获取锁
     *
     * @param objectName objectName
     * @return RLock
     */
    public RLock getRLock(String objectName) {
        return redissonClient.getLock(objectName);
    }

    /**
     * 获取读取锁
     *
     * @param objectName objectName
     * @return RReadWriteLock
     */
    public RReadWriteLock getRWLock(String objectName) {
        return redissonClient.getReadWriteLock(objectName);
    }

    /**
     * 获取原子数
     *
     * @param objectName objectName
     * @return RAtomicLong
     */
    public RAtomicLong getRAtomicLong(String objectName) {
        return redissonClient.getAtomicLong(objectName);
    }

    /**
     * 获取记数锁
     *
     * @param objectName objectName
     * @return RCountDownLatch
     */
    public RCountDownLatch getRCountDownLatch(String objectName) {
        return redissonClient.getCountDownLatch(objectName);
    }

    /**
     * 获取消息的Topic
     *
     * @param objectName objectName
     * @return RTopic
     */
    public <M> RTopic<M> getRTopic(String objectName) {
        return redissonClient.getTopic(objectName);
    }
}
