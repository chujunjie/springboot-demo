package com.example.springbootdemo.java8;

import java.util.function.Supplier;

/**
 * @Description: 依赖注入来引用底层资源
 * @Author: chujunjie
 * @Date: Create in 23:21 2019/5/30
 * @Modified By
 */
public class SpellChecker {
    private final Dictionary dictionary;

    public SpellChecker(Supplier<? extends Dictionary> supplier) {
        this.dictionary = supplier.get();
    }

    public boolean isValid(String word) {
        return dictionary.check(word);
    }
}
