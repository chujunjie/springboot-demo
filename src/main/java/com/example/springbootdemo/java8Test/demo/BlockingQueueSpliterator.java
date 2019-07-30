package com.example.springbootdemo.java8Test.demo;

import java.util.Spliterator;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

/**
 * @Description: 自定义Spliterator，仅利用流的延迟绑定能力，未定义流的切分策略，
 * 其功能是遍历读取队列中的每个元素
 * @Author: chujunjie
 * @Date: Create in 22:52 2019/7/4
 * @Modified By
 */
public class BlockingQueueSpliterator<T> implements Spliterator<T> {
    private final BlockingQueue<T> queue;

    public BlockingQueueSpliterator(BlockingQueue<T> queue) {
        this.queue = queue;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        T t;
        while (true) {
            try {
                t = queue.take();
                break;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (t != ForkingStreamConsumer.END_OF_STREAM) {
            action.accept(t);
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return 0;
    }

    @Override
    public int characteristics() {
        return 0;
    }
}
