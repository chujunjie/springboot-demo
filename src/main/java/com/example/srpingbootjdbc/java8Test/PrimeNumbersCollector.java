package com.example.srpingbootjdbc.java8Test;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static com.example.srpingbootjdbc.java8Test.Prime.isPrime;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 21:27 2019/6/21
 * @Modified By
 */
public class PrimeNumbersCollector implements Collector<Integer, Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> {
    @Override
    public Supplier<Map<Boolean, List<Integer>>> supplier() {
        return () -> new HashMap<Boolean, List<Integer>>() {{
            put(true, new ArrayList<>());
            put(false, new ArrayList<>());
        }};
    }

    @Override
    public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
        return (Map<Boolean, List<Integer>> acc, Integer candidate) -> {
            acc.get(isPrime(acc.get(true), candidate)) // 将n-1的质数流及n当前值传给isPrime判断
                    .add(candidate); // 将被测数添加到相应列表中
        };
    }

    @Override
    public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
        return (Map<Boolean, List<Integer>> map1, Map<Boolean, List<Integer>> map2) -> {
            map1.get(true).addAll(map2.get(true));
            map1.get(false).addAll(map2.get(false));
            return map1;
        };

        // 本例是有序的质数流，无法并行使用，使用有参构造，并将此方法抛出异常更为合适
//        throw new UnsupportedOperationException("无法并行计算");
    }

    @Override
    public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
        return Function.identity(); // 收集结果无需转换
    }

    @Override
    public Set<Characteristics> characteristics() {
        // 1.UNORDERED：有序的质数流，规约结果受遍历顺序影响，故不选
        // 2.CONCURRENT：不支持多线程同时调用
        // 3.IDENTITY_FINISH：表明累加器对象直接作为规约结果，本例中都为map
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
    }
}
