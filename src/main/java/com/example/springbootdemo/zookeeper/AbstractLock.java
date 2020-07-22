package com.example.springbootdemo.zookeeper;

import org.I0Itec.zkclient.ZkClient;

/**
 * zk锁抽象类
 * <p>
 * 获取锁{@link AbstractLock#lock}
 * 释放锁{@link AbstractLock#unlock}
 *
 * @author chujunjie
 * @date Create in 21:35 2020/7/22
 */
public abstract class AbstractLock {

    /**
     * zk客户端
     */
    protected ZkClient zkClient;

    protected AbstractLock(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    /**
     * 获取锁
     */
    public void lock() {
        String threadName = Thread.currentThread().getName();
        if (tryLock()) {
            System.out.println(threadName + "-获取锁成功");
        } else {
            System.out.println(threadName + "-获取锁失败，等待...");
            waitLock();
            //递归重新获取锁
            lock();
        }
    }

    /**
     * 释放锁
     */
    public abstract void unlock();

    /**
     * 尝试获取锁
     *
     * @return boolean
     */
    abstract boolean tryLock();

    /**
     * 等待锁
     */
    abstract void waitLock();
}
