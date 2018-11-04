package com.example.srpingbootjdbc.config;

import com.example.srpingbootjdbc.interceptor.MyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description: mvc自定义拦截器
 * @Author: chujunjie
 * @Date: Create in 20:04 2018/11/3
 * @Modified By
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor());
    }
}
