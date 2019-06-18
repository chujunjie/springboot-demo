package com.example.srpingbootjdbc.java8Test;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.example.srpingbootjdbc.java8Test.Dish.CaloricLevel.*;
import static com.example.srpingbootjdbc.java8Test.Dish.menu;
import static java.util.stream.Collectors.*;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 15:50 2018/9/21
 * @Modified By
 */
public class StreamTest {
    public static void main(String[] args) {
        // 1.获取前三个卡路里大于300的食物
        String threeHighCaloricDishNames = menu.stream() // parallelStream() 并行化处理
                .filter(d -> d.getCalories() > 300)
                .map(Dish::getName) // 映射，生成新的元素
                .limit(3)
                .collect(joining(", "));
        System.out.println(threeHighCaloricDishNames);

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
        int sum = menu.stream()
                .mapToInt(Dish::getCalories) // 数值流，避免暗含的装箱成本，使用boxed()转换回对象流
                .sum();
        System.out.println(reduce);

        // 7.数值流，勾股数
        IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a ->
                        IntStream.rangeClosed(a, 100)
                                .mapToObj(b -> new double[]{a, b, Math.sqrt(a * a + b * b)})
                                .filter(t -> t[2] % 1 == 0))
                .limit(5)
                .forEach(t -> System.out.println((int) t[0] + ", " + (int) t[1] + ", " + (int) t[2]));

        // 8.无限流
        List<Integer> fibonacci = Stream.iterate(new int[]{0, 1},
                t -> new int[]{t[1], t[0] + t[1]}) // 迭代生成斐波那契数列
                .limit(10)
                .map(t -> t[0])
                .collect(toList());
        System.out.println(fibonacci);
        Stream.generate(Math::random) // 生成产生随机双精度数
                .limit(5)
                .forEach(System.out::println);

        // 9.分组
        Map<Dish.Type, Map<Dish.CaloricLevel, List<Dish>>> groupingBy = menu.stream()
                .collect(groupingBy(Dish::getType,
                        groupingBy(dish -> { // 二级分组
                            if (dish.getCalories() <= 400)
                                return LOW;
                            else if (dish.getCalories() <= 700)
                                return NORMAL;
                            else return HIGH;
                        })));
        System.out.println(groupingBy);
        Map<Dish.Type, Set<Dish.CaloricLevel>> groupingBy2 = menu.stream()
                .collect(groupingBy(Dish::getType,
                        mapping(dish -> {
                            if (dish.getCalories() <= 400)
                                return LOW;
                            else if (dish.getCalories() <= 700)
                                return NORMAL;
                            else return HIGH;
                        }, toCollection(HashSet::new)))); // 控制收集的集合类型
        System.out.println(groupingBy2);
    }
}

