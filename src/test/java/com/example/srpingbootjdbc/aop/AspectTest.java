package com.example.srpingbootjdbc.aop;

import com.example.srpingbootjdbc.springAOP.aspect.AspectConfig;
import com.example.srpingbootjdbc.springAOP.aspect.Goods;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 22:46 2019/2/19
 * @Modified By
 */
public class AspectTest {

    private AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AspectConfig.class);

    @Test
    public void test01() {
        Goods goods = (Goods)applicationContext.getBean("goods");
        goods.cost(10, 2);
        goods.cost(10, 0);
    }
}
