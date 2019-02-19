package com.example.srpingbootjdbc.springIOC.registercomponent;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Description: @Conditional:根据条件实现Bean的动态注册
 *               @Import: 快速注册组件
 *               ImportSelector: 返回需要导入组件的全类名数组
 *               ImportBeanDefinitionRegistrar: 手动注册bean到容器
 * @Author: chujunjie
 * @Date: Create in 20:33 2019/2/13
 * @Modified By
 */
@Import(Person.class) // 快速导入组件
@Configuration
public class BeanConfig {

    @Bean("bill")
    @Conditional({WindowsCondition.class})
    public Person person01() {
        return new Person("bill gates", 50);
    }

    @Bean("linus")
    @Conditional({LinuxCondition.class})
    public Person person02() {
        return new Person("linus", 50);
    }

    @Bean
    public FactoryBean factoryBean() {
        return new PersonFactoryBean();
    }
}
