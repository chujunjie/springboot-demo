package com.example.springbootdemo.java8Test.anync;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 价格和折扣映射类
 * @Author: chujunjie
 * @Date: Create in 21:51 2019/6/28
 * @Modified By
 */
@AllArgsConstructor
@Getter
public class Quote {
    private final String shopName;
    private final double price;
    private final Discount.Code discountCode;

    public static Quote parse(String s) {
        String[] split = s.split(":");
        String shopName = split[0];
        double price = Double.parseDouble(split[1]);
        Discount.Code discountCode = Discount.Code.valueOf(split[2]);
        return new Quote(shopName, price, discountCode);
    }
}
