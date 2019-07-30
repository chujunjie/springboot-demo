package com.example.springbootdemo.springIOC.registercomponent;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 20:23 2019/2/13
 * @Modified By
 */
public class WindowsCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 获取当前的运行环境
        Environment environment = context.getEnvironment();
        String property = environment.getProperty("os.name");
        if (null == property)
            return false;
        return property.contains("Windows");
    }

}
