package com.example.srpingbootjdbc.springannotation.lifecycle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @Description: 自定义bean的生命周期：
 *                            1. 通过@Bean指定initMethod和destroyMethod
 *                            2. Bean实现InitializingBean和DisposableBean来实现初始化和销毁逻辑
 *                            3. JSR250: @PostConstruct在bean创建完成并完成属性赋值后执行，@PreDestroy在bean销毁之前，标注在方法上
 *                            4. BeanPostProcessor接口，统一后置处理器，初始化前后做处理任务，AbstractAutowireCapableBeanFactory.initializeBean
 *                               执行bean的初始化在bean的属性赋值之后，AbstractAutowireCapableBeanFactory.doCreateBean()中先执行populateBean，
 *                               后执行initializeBean
 *
 * spring底层对BeanPostProcessor的使用：
 *                                   ApplicationContextAwareProcessor调用postProcessBeforeInitialization对实现指定Aware接口的Bean执行对应方法
 *                                   BeanValidationPostProcessor完成Bean的属性赋值之后进行数据校验
 *                                   InitDestroyAnnotationBeanPostProcessor实现第三种bean生命周期管理
 *                                   AutowiredAnnotationBeanPostProcessor实现@AutoWired自动装配
 * @Author: chujunjie
 * @Date: Create in 22:02 2019/2/15
 * @Modified By
 */
@Configuration
public class LifeCycleConfig {

//    @Scope(value = "prototype")
    @Bean(initMethod = "init", destroyMethod = "destroy") // 指定初始化和销毁调用方法
    public Car car() {
        return new Car();
    }

    @Bean
    public Plane plane() {
        return new Plane();
    }
}
