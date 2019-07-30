package com.example.springbootdemo.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {

    //mybatis配置定制器
    @Bean
    public ConfigurationCustomizer configurationCustomizer(){
        return configuration -> {
            configuration.setMapUnderscoreToCamelCase(true); // 开启驼峰命名
        };
    }
}
