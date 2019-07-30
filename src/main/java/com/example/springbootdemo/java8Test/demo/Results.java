package com.example.springbootdemo.java8Test.demo;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 22:30 2019/7/4
 * @Modified By
 */
public interface Results {
    <R> R get(Object obj);
}
