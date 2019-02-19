package com.example.srpingbootjdbc.springAOP.aspect;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 22:10 2019/2/19
 * @Modified By
 */
public class Goods {

    public int cost(int totalPrice, int amount) {
        System.out.println("Goods...cost...");
        return totalPrice / amount;
    }
}
