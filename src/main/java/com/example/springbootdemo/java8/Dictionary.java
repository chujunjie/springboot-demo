package com.example.springbootdemo.java8;

import java.util.Comparator;

import static java.util.Comparator.comparingInt;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 23:25 2019/5/30
 * @Modified By
 */
public class Dictionary implements Comparable<Dictionary> {

    private int order;

    private int next;

    private static final Comparator<Dictionary> COMPARATOR =
            comparingInt((Dictionary dic) -> dic.order)
                    .thenComparingInt(dic -> dic.next);

    public boolean check(String word) {
        return true;
    }

    @Override
    public int compareTo(Dictionary o) {
        return COMPARATOR.compare(this, o);
    }
}
