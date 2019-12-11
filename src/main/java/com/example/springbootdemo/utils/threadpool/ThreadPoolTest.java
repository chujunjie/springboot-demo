package com.example.springbootdemo.utils.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 0:00 2019/12/12
 * @Modified By
 */
@Slf4j
public class ThreadPoolTest {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10);
        CustomThreadPool pool = new CustomThreadPool(3, 5, 1, TimeUnit.SECONDS, queue, () -> log.info("任务执行完毕"));

        for (int i = 0; i < 10; i++) {
            final int j = i;
            pool.execute(() -> System.out.println(j));
        }

        log.info("============休眠前线程池活跃线程数={}============", pool.getWorkerCount());

        TimeUnit.SECONDS.sleep(5);
        log.info("============休眠后线程池活跃线程数={}============", pool.getWorkerCount());

        for (int i = 0; i < 3; i++) {
            final int j = i;
            pool.execute(() -> System.out.println(j + 100));
        }

        pool.shutdown();
    }
}
