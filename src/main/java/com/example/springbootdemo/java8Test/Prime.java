package com.example.springbootdemo.java8Test;

import java.util.List;
import java.util.function.Predicate;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 21:38 2019/6/21
 * @Modified By
 */
public class Prime {

    /**
     * 判断是否为质数
     *
     * @param primes    记录n-1流中的质数列表
     * @param candidate 当前n值
     * @return
     */
    public static boolean isPrime(List<Integer> primes, int candidate) {
        int candidateRoot = (int) Math.sqrt((double) candidate); // 平方根
        return takeWhile(primes, i -> i <= candidateRoot) // 仅将不大于平方根的质数作为除数
                .stream()
                .noneMatch(p -> candidate % p == 0);

    }

    /**
     * 返回满足谓词条件的最大子列表
     *
     * @param list
     * @param p
     * @param <T>
     * @return
     */
    private static <T> List<T> takeWhile(List<T> list, Predicate<T> p) {
        int i = 0;
        for (T item : list) {
            if (!p.test(item))
                return list.subList(0, i);
            i++;
        }
        return list;
    }
}
