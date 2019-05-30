package com.example.srpingbootjdbc.java8Test;

import java.util.List;

import static com.example.srpingbootjdbc.java8Test.Dish.menu;
import static java.util.stream.Collectors.toList;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 15:50 2018/9/21
 * @Modified By
 */
public class StreamTest {
    public static void main(String[] args) {
        List<String> threeHighCaloricDishNames = menu.stream()
                .filter(d -> d.getCalories() > 300)
                .map(Dish::getName)
                .limit(3)
                .collect(toList());
        threeHighCaloricDishNames.forEach(System.out::println);

        SpellChecker checker = new SpellChecker(Dictionary::new);
        checker.isValid("hello world");
    }
}

