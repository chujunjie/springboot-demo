package com.example.springbootdemo.java8Test.anync;

import lombok.Getter;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 21:50 2019/6/27
 * @Modified By
 */
@Getter
public class Shop {

    private static final Random random = new Random();

    private String name;

    public Shop(String name) {
        this.name = name;
    }

    public double getPrice(String product) {
        return calculatePrice(product);
    }

    /**
     * 异步获取价格
     *
     * @param product
     * @return
     */
    public Future<Double> getPriceAsync(String product) {
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }

    /**
     * 获取价格和折扣
     *
     * @param product
     * @return
     */
    public String getPriceAndDiscount(String product) {
        double price = calculatePrice(product);
        Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
        return String.format("%s:%.2f:%s", name, price, code);
    }

    /**
     * 计算价格
     *
     * @param product
     * @return
     */
    private double calculatePrice(String product) {
        if ("car".equals(product))
            throw new RuntimeException("We don't have this product!");
        delay();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    /**
     * 模拟网络延时
     */
    public static void delay() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }
}
