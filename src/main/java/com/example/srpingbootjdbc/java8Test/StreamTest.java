package com.example.srpingbootjdbc.java8Test;

import java.util.Arrays;
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
        // 1.获取前三个卡路里大于300的食物
        List<String> threeHighCaloricDishNames = menu.stream() // parallelStream() 并行化处理
                .filter(d -> d.getCalories() > 300)
                .map(Dish::getName) // 映射，生成新的元素
                .limit(3)
                .collect(toList());
        threeHighCaloricDishNames.forEach(System.out::println);

        // 2.工厂获取字典，并注入到拼写检查器
        SpellChecker checker = new SpellChecker(Dictionary::new);
        checker.isValid("hello world");

        // 3.Optional使用
        List<String> list = Arrays.asList("1", "2", "3");
        String result = MyComparator.max(list).orElse("null...");
        System.out.println(result);
        String s = list.stream()
                .filter(x -> Integer.parseInt(x) > 10)
                .findAny()
                .orElse("null...");
        System.out.println(s);

        // 4.获取字符串列表中所有字符（不重复）
        List<String> words = Arrays.asList("Java8", "Lambdas", "In", "Action");
        List<String> uniqueCharacters = words.stream()
                .map(word -> word.split(""))
                .flatMap(Arrays::stream) // 流的扁平化，将一个流中的每个值都换成另一个流，并将所有的流连接成一个流
                .distinct() // 去重
                .collect(toList()); // 也可以用toSet()代替去重
        uniqueCharacters.forEach(System.out::print);
        System.out.println();

        // 5.给定两个数字列表，返回能被3整除的数对
        List<Integer> nums1 = Arrays.asList(1, 2, 3);
        List<Integer> nums2 = Arrays.asList(3, 4);
        List<int[]> pairs = nums1.stream()
                .flatMap(i -> nums2.stream() // 使用两个map会产生Stream<Stream<int[]>>
                        .filter(j -> (i + j) % 3 == 0)
                        .map(j -> new int[]{i, j}))
                .collect(toList());
        pairs.forEach(x -> System.out.print(Arrays.toString(x)));
        System.out.println();

        // 6.归约，map-reduce思想，元素求和、求最大/最小值
        Integer reduce = menu.stream()
                .map(Dish::getCalories)
                .reduce(0, Integer::sum); // reduce(Integer::max) 最大值
        System.out.println(reduce);
    }
}

