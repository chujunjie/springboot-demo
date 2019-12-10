package com.example.springbootdemo.java8.demo;

import com.example.springbootdemo.java8.Dish;

import java.util.stream.Stream;

import static com.example.springbootdemo.java8.Dish.menu;
import static java.util.stream.Collectors.*;

/**
 * @Description: 复制流，使其能在一个流上并发执行多个终端操作
 * 适用场景：一个流操作涉及大量的I/O，譬如一个巨型文件
 * @Author: chujunjie
 * @Date: Create in 23:14 2019/7/4
 * @Modified By
 */
public class StreamForkerDemo {
    public static void main(String[] args) {
        Stream<Dish> menuStream = menu.stream();
        Results results = new StreamForker<>(menuStream)
                .fork("shortMenu", s -> s.map(Dish::getName)
                        .collect(joining(", ")))
                .fork("totalCalories", s -> s.mapToInt(Dish::getCalories).sum())
                .fork("mostCaloriesDish", s -> s.mapToInt(Dish::getCalories).max())
                .fork("dishesByType", s -> s.collect(groupingBy(Dish::getType)))
                .getResults();
        System.out.println("Short menu: " + results.get("shortMenu"));
        System.out.println("Total calories: " + results.get("totalCalories"));
        System.out.println("Most caloric dish: " + results.get("mostCaloriesDish"));
        System.out.println("Dishes by Type: " + results.get("dishesByType"));
    }
}
