package com.example.springbootdemo.utils.threadpool;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description: 自定义线程池
 * @Author: chujunjie
 * @Date: Create in 19:36 2019/12/11
 * @Modified By
 */
@Slf4j
public class CustomThreadPool {

    /**
     * lock
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 核心线程数
     */
    private volatile int corePoolSize;

    /**
     * 最大线程数
     */
    private volatile int maximumPoolSize;

    /**
     * 线程保活时间
     */
    private volatile long keepAliveTime;

    /**
     * time unit
     */
    private TimeUnit unit;

    /**
     * 阻塞队列
     */
    private final BlockingQueue<Runnable> workQueue;

    /**
     * workers
     */
    private final Set<Worker> workers;

    /**
     * 线程池任务全部执行完毕后的通知组件
     */
    private final Object shutDownNotify = new Object();

    /**
     * 通知接口
     */
    private Notify notify;

    /**
     * 关闭标志位
     */
    private volatile boolean isShutDown;

    /**
     * 提交到线程池中的任务总数
     */
    private AtomicInteger totalTask = new AtomicInteger();

    public CustomThreadPool(int corePoolSize,
                            int maximumPoolSize,
                            long keepAliveTime,
                            TimeUnit unit,
                            BlockingQueue<Runnable> workQueue,
                            Notify notify) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.unit = unit;
        this.workQueue = workQueue;
        this.notify = notify;

        workers = new ConcurrentHashSet<>();
    }

    /**
     * 执行有返回值的任务
     *
     * @param callable callable
     * @return T
     */
    public <T> Future<T> submit(Callable<T> callable) {
        FutureTask<T> future = new FutureTask<>(callable);
        execute(future);
        return future;
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(@NonNull Runnable task) {
        if (isShutDown) {
            log.info("线程池以关闭，无法提交任务");
            return;
        }

        // 提交任务数
        totalTask.incrementAndGet();

        if (workers.size() < corePoolSize) {
            addWorker(task);
            return;
        }

        boolean offer = workQueue.offer(task);
        if (!offer) { // 写入队列失败
            if (workers.size() < maximumPoolSize) {
                addWorker(task);
                return;
            }
            log.info("超出最大线程数");
            try {
                // 以阻塞方式放入队列，也可以跟JUC一样执行拒绝策略
                workQueue.put(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取工作线程数量
     *
     * @return int
     */
    public int getWorkerCount() {
        return workers.size();
    }

    /**
     * 任务执行完毕后关闭线程池
     */
    public void shutdown() {
        isShutDown = true;
        tryClose(true);
    }

    /**
     * 立即关闭线程池，会造成任务丢失
     */
    public void shutDownNow() {
        isShutDown = true;
        tryClose(false);
    }

    /**
     * 阻塞等到任务执行完毕
     */
    public void mainNotify() {
        synchronized (shutDownNotify) {
            while (totalTask.get() > 0) {
                try {
                    shutDownNotify.wait();
                    if (null != notify) {
                        notify.notifyListen();
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    /**
     * 添加worker
     *
     * @param task task
     */
    private void addWorker(Runnable task) {
        Worker worker = new Worker(task, true);
        worker.startTask();
        workers.add(worker);
    }

    /**
     * 从队列中获取任务
     */
    private Runnable getTask() {
        // 关闭标志位和任务数判断
        if (isShutDown && totalTask.get() == 0)
            return null;

        lock.lock();

        Runnable task;

        try {
            // 大于核心线程数时，根据保活时间获取任务
            if (workers.size() > corePoolSize) {
                task = workQueue.poll(keepAliveTime, unit);
            } else {
                task = workQueue.take();
            }
        } catch (InterruptedException e) {
            return null;
        } finally {
            lock.unlock();
        }

        return task;
    }

    /**
     * 关闭线程池
     *
     * @param isTry true 尝试关闭      --> 会等待所有任务执行完毕
     *              false 立即关闭线程池--> 任务有丢失的可能
     */
    private void tryClose(boolean isTry) {
        if (!isTry) {
            closeAllTask();
        } else {
            if (isShutDown && totalTask.get() == 0)
                closeAllTask();
        }
    }

    /**
     * 关闭所有任务
     */
    private void closeAllTask() {
        for (Worker worker : workers) {
            worker.close();
        }
    }

    /**
     * 工作线程
     */
    private final class Worker extends Thread {

        /**
         * 任务
         */
        private Runnable task;

        /**
         * thread
         */
        private Thread thread;

        /**
         * true --> 创建新的线程执行
         * false --> 从队列里获取线程执行
         */
        private boolean isNewTask;

        Worker(Runnable task, boolean isNewTask) {
            this.task = task;
            this.isNewTask = isNewTask;
            thread = this;
        }

        @Override
        public void run() {
            Runnable task = isNewTask ? this.task : null;

            // 任务执行情况标志位
            boolean compile = true;

            try {
                while (null != task || null != (task = getTask())) {
                    try {
                        // 执行任务
                        task.run();
                    } catch (Exception e) {
                        compile = false;
                        throw e;
                    } finally {
                        // 任务执行完毕
                        task = null;
                        if (totalTask.decrementAndGet() == 0) {
                            synchronized (shutDownNotify) {
                                shutDownNotify.notify();
                            }
                        }
                    }
                }
            } finally {
                // 释放线程
                workers.remove(this);

                // 任务执行失败直接释放掉当前线程并且新建一个worker
                if (!compile) {
                    addWorker(null);
                }

                tryClose(true);
            }
        }

        /**
         * 开始任务
         */
        void startTask() {
            thread.start();
        }

        /**
         * 中断线程
         */
        public void close() {
            thread.interrupt();
        }

    }

    /**
     * ConcurrentHashSet
     *
     * @param <T>
     */
    private final class ConcurrentHashSet<T> extends AbstractSet<T> {

        /**
         * 计数
         */
        private AtomicInteger count = new AtomicInteger();

        /**
         * value
         */
        private final Object PRESENT = new Object();

        /**
         * map
         */
        private Map<T, Object> map = new ConcurrentHashMap<>();

        @Override
        public boolean add(T t) {
            count.incrementAndGet();
            return map.put(t, PRESENT) == null;
        }

        @Override
        public boolean remove(Object o) {
            count.decrementAndGet();
            return map.remove(o) == PRESENT;
        }

        @Override
        public Iterator<T> iterator() {
            return map.keySet().iterator();
        }

        @Override
        public int size() {
            return count.get();
        }
    }
}
