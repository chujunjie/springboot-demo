package com.example.springbootdemo.java8;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: 测试菜单
 * @Author: chujunjie
 * @Date: Create in 22:55 2018/9/24
 * @Modified By
 */
@Setter
@Getter
public class Dish {
    private String name;
    private boolean vegetarian;
    private int calories;
    private Dish.Type type;

    public Dish(String name, boolean vegetarian, int calories, Dish.Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }

    public enum Type {
        MEAT,
        FISH,
        OTHER
    }

    public enum CaloricLevel {
        LOW,
        NORMAL,
        HIGH
    }

    public static final List<Dish> menu =
            Arrays.asList(new Dish("pork", false, 800, Dish.Type.MEAT),
                    new Dish("beef", false, 700, Dish.Type.MEAT),
                    new Dish("chicken", false, 400, Type.MEAT),
                    new Dish("french fries", true, 530, Dish.Type.OTHER),
                    new Dish("rice", true, 350, Dish.Type.OTHER),
                    new Dish("season fruit", true, 120, Dish.Type.OTHER),
                    new Dish("pizza", true, 550, Dish.Type.OTHER),
                    new Dish("prawns", false, 400, Dish.Type.FISH),
                    new Dish("salmon", false, 450, Dish.Type.FISH));
}
