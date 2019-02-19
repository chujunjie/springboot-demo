package com.example.srpingbootjdbc.springIOC.registercomponent;

import org.springframework.beans.factory.FactoryBean;

/**
 * @Description: 创建bean的工厂类
 * @Author: chujunjie
 * @Date: Create in 21:24 2019/2/13
 * @Modified By
 */
public class PersonFactoryBean implements FactoryBean<Person> {

    /**
     * 返回一个Person对象，并将该对象加入到容器中
     * @return
     * @throws Exception
     */
    @Override
    public Person getObject() throws Exception {
        System.out.println("PersonFactoryBean...getObject...");
        return new Person();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    /**
     * 是否为单例
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
