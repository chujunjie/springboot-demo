package com.example.springbootdemo.redis.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: redisson配置
 * @Author: chujunjie
 * @Date: Create in 22:47 2018/10/10
 * @Modified By
 */
@Configuration
public class RedissonConfig {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedissonClient getRedisson(){

        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port)
                .setPassword(password);
        //添加主从配置
//        config.useMasterSlaveServers().setMasterAddress("")
//                .setPassword("")
//                .addSlaveAddress(new String[]{"",""});
        return Redisson.create(config);
    }

}
