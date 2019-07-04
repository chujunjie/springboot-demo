package com.example.srpingbootjdbc.java8Test.demo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 22:41 2019/7/4
 * @Modified By
 */
@SuppressWarnings("unchecked")
public class ForkingStreamConsumer<T> implements Consumer<T>, Results {
    static final Object END_OF_STREAM = new Object();

    private final List<BlockingQueue<T>> queues;
    private final Map<Object, Future<?>> actions; // Future中包含对应操作的处理结果

    public ForkingStreamConsumer(List<BlockingQueue<T>> queues, Map<Object, Future<?>> actions) {
        this.queues = queues;
        this.actions = actions;
    }

    @Override
    public <R> R get(Object key) {
        try {
            return ((Future<R>) actions.get(key)).get(); // 等待Future完成计算后返回由特定键标识的处理结果
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void accept(T t) {
        queues.forEach(b -> b.add(t)); // 将流中遍历的元素添加到所有队列中
    }

    public void finish() {
        accept((T) END_OF_STREAM); // 当原始流中所有元素都添加到队列之后，在末尾添加一个默认元素标识流结束
    }
}
