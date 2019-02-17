package com.example.srpingbootjdbc;

import com.example.srpingbootjdbc.springannotation.lifecycle.LifeCycleConfig;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 22:05 2019/2/15
 * @Modified By
 */
public class LifeCycleTest {

    private AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(LifeCycleConfig.class);

    @Test
    public void test01() {

        // @Scope 多实例bean在获取bean时才创建，并执行初始化方法
//        applicationContext.getBean("car");

        applicationContext.getBean("animal");
        // 多实例bean在容器关闭时不销毁
        applicationContext.close();
    }
}
