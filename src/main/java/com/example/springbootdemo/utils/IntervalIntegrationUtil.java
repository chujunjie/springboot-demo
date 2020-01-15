package com.example.springbootdemo.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.TreeSet;

/**
 * @Description: 区间整合
 * @Author: chujunjie
 * @Date: Create in 22:46 2020/1/15
 * @Modified By
 */
public class IntervalIntegrationUtil {

    private static final String COMMA = ",";
    private static final String MINUS = "-";
    private static final String INVALID_DESC = "非法格式";

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 1024;

    /**
     * revise
     *
     * @param original original
     * @return String
     */
    public static String revise(String original) {
        if (StringUtils.isBlank(original)) {
            return null;
        }

        TreeSet<Integer> treeSet = new TreeSet<>();
        StringBuilder sb = new StringBuilder();

        try {
            String[] split = original.split(COMMA);
            for (String str : split) {
                if (StringUtils.isEmpty(str)) {
                    continue;
                }
                if (str.contains(MINUS)) {
                    String[] split1 = str.split(MINUS);
                    if (split1.length != 2) {
                        throw new IllegalArgumentException(INVALID_DESC);
                    }

                    int left = Integer.parseInt(split1[0]);
                    int right = Integer.parseInt(split1[1]);
                    if (left < MIN_VALUE || right < MIN_VALUE || right > MAX_VALUE | left >= right) {
                        throw new IllegalArgumentException(INVALID_DESC);
                    }
                    for (int i = left; i <= right; i++) {
                        treeSet.add(i);
                    }
                } else {
                    treeSet.add(Integer.valueOf(str));
                }
            }


            if (!treeSet.isEmpty()) {
                Integer slow = treeSet.first();
                Integer fast = treeSet.first();
                for (Integer i : treeSet) {
                    if (i - fast <= 1) {
                        fast = i;
                        continue;
                    }

                    if (slow.equals(fast)) {
                        sb.append(slow.toString()).append(COMMA);
                    } else {
                        sb.append(slow).append(MINUS).append(fast).append(COMMA);
                    }

                    slow = i;
                    fast = i;
                }

                if (slow.equals(fast)) {
                    sb.append(slow.toString());
                } else {
                    sb.append(slow).append(MINUS).append(fast);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(INVALID_DESC);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String str = "1-6,25,88,45,66-99,88-200,400,200";
        long start = System.currentTimeMillis();
        System.out.println(revise(str));
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
