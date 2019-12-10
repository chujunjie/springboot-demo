package com.example.springbootdemo.spring.ioc.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 21:46 2019/2/17
 * @Modified By
 */
public class Animal implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        System.out.println("拿到Ioc容器");
    }
}
