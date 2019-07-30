package com.example.springbootdemo.java8Test.anync;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 22:02 2019/6/27
 * @Modified By
 */
public class AsyncTest {

    private static final List<Shop> shops = new ArrayList<>();

    static {
        int processors = Runtime.getRuntime().availableProcessors(); // 获取cpu核数
        for (int i = 1; i <= processors + 1; i++) { // 模拟比cpu核数多1的任务数
            shops.add(new Shop("Shop" + i));
        }
    }

    /**
     * 自定义执行器
     */
    private static final Executor executor =
            Executors.newFixedThreadPool(Math.min(shops.size(), 100),
                    r -> {
                        Thread thread = new Thread(r);
                        thread.setDaemon(true);
                        return thread;
                    });

    public static void main(String[] args) {

        // 1.并行流版本
        findPrices("HuaWei p30", AsyncTest::findPricesWithParallel);

        // 2.CompletableFuture异步版本，使用默认执行器
        findPrices("MacBook pro", AsyncTest::findPricesWithCompletableFuture);

        // 3.自定义执行器的异步版本，更具灵活性
        findPrices("iphone X", AsyncTest::findPricesWithExecutor);

        // 4.获取折后价格
        findPrices("Samsung s10", AsyncTest::findDiscountedPrices);

        // 5.响应completion事件，流中有事件完成则先处理
        findPricesStream("xiaomi 9");
    }


    /**
     * 并行流获取所有商店的价格
     *
     * @param product
     * @return
     */
    private static List<String> findPricesWithParallel(String product) {
        return shops.parallelStream()
                .map(shop -> String.format("%s price is %.2f",
                        shop.getName(), shop.getPrice(product)))
                .collect(toList());
    }

    /**
     * CompletableFuture获取所有商店的价格
     *
     * @param product
     * @return
     */
    private static List<String> findPricesWithCompletableFuture(String product) {
        List<CompletableFuture<String>> futures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> String.format("%s price is %.2f",
                                shop.getName(), shop.getPrice(product))))
                .collect(toList());

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    /**
     * CompletableFuture获取所有商店的价格
     *
     * @param product
     * @return
     */
    private static List<String> findPricesWithExecutor(String product) {
        List<CompletableFuture<String>> futures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> String.format("%s price is %.2f",
                                shop.getName(), shop.getPrice(product)),
                        executor))
                .collect(toList());

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    /**
     * 获取折扣后的商品
     *
     * @param product
     * @return
     */
    private static List<String> findDiscountedPrices(String product) {
        List<CompletableFuture<String>> priceFutures = shops.stream()
                // 1.获取价格
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> shop.getPriceAndDiscount(product), executor))
                // 2.解析报价，同步操作
                .map(future -> future.thenApply(Quote::parse))
                // 3.计算折后价格，可以使用thenComposeAsync异步化，但这里依赖第一步中返回的价格
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(
                                () -> Discount.applyDiscount(quote), executor)))
                .collect(toList());

        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    /**
     * 获取价格并打印执行时间
     *
     * @param product
     * @param function
     * @param <T>
     * @param <R>
     */
    private static <T, R> void findPrices(T product, Function<T, R> function) {
        long start = System.nanoTime();
        System.out.println(function.apply(product));
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");
    }

    /**
     * 响应completion事件，先执行完的先打印
     *
     * @param product
     * @return
     */
    private static void findPricesStream(String product) {
        long start = System.nanoTime();
        CompletableFuture[] futures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> shop.getPriceAndDiscount(product), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(
                                () -> Discount.applyDiscount(quote), executor)))
                // 4.使用CompletableFuture完成执行后的返回值，打印
                .map(f -> f.thenAccept(s -> System.out.println(s + " (done in " +
                        (System.nanoTime() - start) / 1_000_000 + " msecs)")))
                // 5.将先执行完的任务放到空数组，以等待其他任务完成
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
        System.out.println("All shops returned results or time out");
    }
}
