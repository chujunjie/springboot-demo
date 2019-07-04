package com.example.srpingbootjdbc.java8Test.demo;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @Description: 处理流中的元素，将它们分发到多个BlockingQueues中处理，
 * BlockingQueues的数量和通过fork方法提交的操作数一致
 * @Author: chujunjie
 * @Date: Create in 22:24 2019/7/4
 * @Modified By
 */
public class StreamForker<T> {
    private final Stream<T> stream;

    /**
     * value:对流进行处理，转变为代表操作结果的任意类型
     */
    private final Map<Object, Function<Stream<T>, ?>> forks = new HashMap<>();

    public StreamForker(Stream<T> stream) {
        this.stream = stream;
    }

    public StreamForker<T> fork(Object key, Function<Stream<T>, ?> f) {
        forks.put(key, f);
        return this;
    }

    public Results getResults() {
        ForkingStreamConsumer<T> consumer = build();
        try {
            stream.sequential().forEach(consumer); // 顺序执行
        } finally {
            consumer.finish();
        }
        return consumer;

    }

    private ForkingStreamConsumer<T> build() {
        List<BlockingQueue<T>> queues = new ArrayList<>(); // 创建由队列组成的列表，每个队列对应一个操作
        Map<Object, Future<?>> actions =
                forks.entrySet().stream() // 建立用于标识操作的键与包含操作结果的Future之间的映射关系
                        .reduce(new HashMap<>(),
                                (map, e) -> {
                                    map.put(e.getKey(),
                                            getOperationResult(queues, e.getValue()));
                                    return map;
                                },
                                (m1, m2) -> {
                                    m1.putAll(m2);
                                    return m1;
                                });
        return new ForkingStreamConsumer<>(queues, actions);
    }

    private Future<?> getOperationResult(List<BlockingQueue<T>> queues, Function<Stream<T>, ?> f) {
        BlockingQueue<T> queue = new LinkedBlockingQueue<>();
        // 1.创建一个队列，并将其添加到队列列表中
        queues.add(queue);
        // 2.创建一个Spliterator，遍历队列中的元素
        Spliterator<T> spliterator = new BlockingQueueSpliterator<>(queue);
        // 3.创建一个流，将Spliterator作为数据源
        Stream<T> source = StreamSupport.stream(spliterator, false);
        // 4.创建一个Future对象，以异步的方式计算流上执行特定函数的结果
        return CompletableFuture.supplyAsync(() -> f.apply(source));
    }
}
