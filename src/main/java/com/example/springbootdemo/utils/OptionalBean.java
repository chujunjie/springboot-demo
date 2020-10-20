package com.example.springbootdemo.utils;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author chujunjie
 * @date Create in 21:51 2020/10/20
 */
public class OptionalBean<T> {

    private static final OptionalBean<?> EMPTY = new OptionalBean<>();

    private final T value;

    private OptionalBean() {
        this.value = null;
    }

    /**
     * 包装一个可能为空的 bean
     *
     * @param value value
     * @param <T>   T
     * @return OptionalBean
     */
    public static <T> OptionalBean<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }

    /**
     * 包装一个不能为空的 bean
     *
     * @param value value
     * @param <T>   T
     * @return OptionalBean
     */
    private static <T> OptionalBean<T> of(T value) {
        return new OptionalBean<>(value);
    }

    /**
     * 空值常量
     *
     * @param <T> T
     * @return OptionalBean
     */
    @SuppressWarnings("unchecked")
    private static <T> OptionalBean<T> empty() {
        return (OptionalBean<T>) EMPTY;
    }

    /**
     * 取出具体的值
     *
     * @return T
     */
    public T get() {
        return Objects.isNull(value) ? null : value;
    }

    /**
     * 取出一个可能为空的对象
     *
     * @param fn  fn
     * @param <R> R
     * @return OptionalBean
     */
    public <R> OptionalBean<R> getBean(Function<? super T, ? extends R> fn) {
        return Objects.isNull(value) ? OptionalBean.empty() : OptionalBean.ofNullable(fn.apply(value));
    }

    /**
     * 如果目标值为空 获取一个默认值
     *
     * @param other other
     * @return T
     */
    public T orElse(T other) {
        return value != null ? value : other;
    }

    /**
     * 如果目标值为空 通过lambda表达式获取一个值
     *
     * @param other other
     * @return T
     */
    public T orElseGet(Supplier<? extends T> other) {
        return value != null ? value : other.get();
    }

    /**
     * 如果目标值为空 抛出一个异常
     *
     * @param exceptionSupplier exceptionSupplier
     * @param <X>               X
     * @return T
     * @throws X X
     */
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value != null) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    public boolean isPresent() {
        return value != null;
    }

    public void ifPresent(Consumer<? super T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }


    /**
     * 空值会抛出空指针
     *
     * @param value value
     */
    private OptionalBean(T value) {
        this.value = Objects.requireNonNull(value);
    }
}
