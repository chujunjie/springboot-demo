package com.example.springbootdemo.spring.ioc.lifecycle;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 22:02 2019/2/15
 * @Modified By
 */
public class Car {

    public Car() {
        System.out.println("car constructor ...");
    }

    public void init() {
        System.out.println("car init ...");
    }

    public void destroy() {
        System.out.println("car destroy ...");
    }
}
