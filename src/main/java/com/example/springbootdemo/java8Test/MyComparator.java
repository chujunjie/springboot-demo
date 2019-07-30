package com.example.springbootdemo.java8Test;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 22:44 2019/6/12
 * @Modified By
 */
public class MyComparator {

    public static <E extends Comparable<E>> Optional<E> max(Collection<E> c) {
        return c.stream().max(Comparator.naturalOrder());
    }
}
