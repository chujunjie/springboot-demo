package com.example.srpingbootjdbc;

import com.example.srpingbootjdbc.springannotation.registercomponent.BeanConfig;
import com.example.srpingbootjdbc.springannotation.registercomponent.Person;
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
public class AnnotationTest {

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
}
