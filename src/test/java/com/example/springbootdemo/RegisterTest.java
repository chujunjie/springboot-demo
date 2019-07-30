package com.example.springbootdemo;

import com.example.springbootdemo.springIOC.registercomponent.BeanConfig;
import com.example.springbootdemo.springIOC.registercomponent.Person;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 20:32 2019/2/13
 * @Modified By
 */
public class RegisterTest {

    private ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanConfig.class);

    @Test
    public void test01() {
        String[] namesForType = applicationContext.getBeanNamesForType(Person.class);
        for (String name : namesForType) {
            System.out.println(name);
        }
        Map<String, Person> persons = applicationContext.getBeansOfType(Person.class);
        System.out.println(persons);
    }

    @Test
    public void test02() {
        printBeans(applicationContext);

        // 工厂获取的bean为调用getObject返回的对象
        Object bean = applicationContext.getBean("factoryBean");
        System.out.println("bean的类型：" + bean.getClass());

        // 获取到工厂bean本身
        Object factoryBean = applicationContext.getBean("&factoryBean");
        System.out.println("bean的类型：" + factoryBean.getClass());
    }

    private void printBeans(ApplicationContext applicationContext) {
        String[] definitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : definitionNames) {
            System.out.println(name);
        }
    }
}
