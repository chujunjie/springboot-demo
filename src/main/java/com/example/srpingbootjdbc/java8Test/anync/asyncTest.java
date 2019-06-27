package com.example.srpingbootjdbc.java8Test.anync;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.toList;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 22:02 2019/6/27
 * @Modified By
 */
public class asyncTest {

    private static final List<Shop> shops = new ArrayList<>();

    static {
        int processors = Runtime.getRuntime().availableProcessors(); // 获取cpu核数
        for (int i = 0; i < processors + 1; i++) { // 模拟比cpu核数多1的任务数
            shops.add(new Shop("Shop" + 1));
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
        long parallelStart = System.nanoTime();
        System.out.println(findPricesWithParallel("HuaWei p30"));
        long parallelDuration = (System.nanoTime() - parallelStart) / 1_000_000;
        System.out.println("Done in " + parallelDuration + " msecs");

        // 2.CompletableFuture异步版本，使用默认执行器
        long completableStart = System.nanoTime();
        System.out.println(findPricesWithCompletableFuture("HuaWei p30", false));
        long completableDuration = (System.nanoTime() - completableStart) / 1_000_000;
        System.out.println("Done in " + completableDuration + " msecs");

        // 3.自定义执行器的异步版本，更具灵活性
        long completableStart2 = System.nanoTime();
        System.out.println(findPricesWithCompletableFuture("HuaWei p30", true));
        long completableDuration2 = (System.nanoTime() - completableStart2) / 1_000_000;
        System.out.println("Done in " + completableDuration2 + " msecs");
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
     * @param useExecutor 使用自定义执行器
     * @return
     */
    private static List<String> findPricesWithCompletableFuture(String product, boolean useExecutor) {

        List<CompletableFuture<String>> futures;

        if (useExecutor) {
            futures = shops.stream()
                    .map(shop -> CompletableFuture.supplyAsync(
                            () -> String.format("%s price is %.2f",
                                    shop.getName(), shop.getPrice(product)),
                            executor))
                    .collect(toList());
        } else {
            futures = shops.stream()
                    .map(shop -> CompletableFuture.supplyAsync(
                            () -> String.format("%s price is %.2f",
                                    shop.getName(), shop.getPrice(product))))
                    .collect(toList());
        }

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }
}
