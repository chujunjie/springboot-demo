package com.example.srpingbootjdbc.springAOP.aspect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 22:45 2019/2/19
 * @Modified By
 */
@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {

    @Bean
    public Goods goods() {
        return new Goods();
    }

    @Bean
    public LogAspects logAspects() {
        return new LogAspects();
    }
}
