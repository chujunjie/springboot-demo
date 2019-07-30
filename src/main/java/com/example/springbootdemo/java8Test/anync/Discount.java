package com.example.springbootdemo.java8Test.anync;

import java.text.DecimalFormat;

import static com.example.springbootdemo.java8Test.anync.Shop.delay;

/**
 * @Description: 折扣服务
 * @Author: chujunjie
 * @Date: Create in 21:44 2019/6/28
 * @Modified By
 */
public class Discount {

    public static String applyDiscount(Quote quote) {
        delay(); // 模拟响应延时
        return quote.getShopName() + " price is " + apply(quote.getPrice(), quote.getDiscountCode());
    }

    private static double apply(double price, Code code) {
        DecimalFormat df = new DecimalFormat(".00");
        return Double.parseDouble(df.format(price * (100 - code.percentage) / 100));
    }

    public enum Code {
        NONE(0),
        SILVER(5),
        GOLD(10),
        PLATINUM(15),
        DIAMOND(20);

        private final int percentage;

        Code(int percentage) {
            this.percentage = percentage;
        }
    }
}
