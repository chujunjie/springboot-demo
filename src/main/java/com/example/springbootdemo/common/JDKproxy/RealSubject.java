package com.example.springbootdemo.common.JDKproxy;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 13:35 2018/7/27
 * @Modified By
 */
public class RealSubject implements Subject{
    @Override
    public void rent() {
        System.out.println("I want to rent my house");
    }

    @Override
    public void hello(String str) {
        System.out.println("hello: " + str);
    }
}

