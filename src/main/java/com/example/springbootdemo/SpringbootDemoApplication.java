package com.example.springbootdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * RabbitMQ:
 * @EnableRabbit 开启自动配置
 * 1.RabbitAutoConfiguration
 * 2.自动配置了连接工厂ConnectionFactory
 * 3.RabbitProperties封装了RabbitMQ的配置
 * 4.RabbitTemplate：用于RabbitMQ发送和接收消息
 * 5.amqpAdmin：RabbitMQ系统功能管理组件
 */

/**
 * elasticsearch:
 * springBoot默认支持两种技术和ES交互
 * 1、Jest(默认不生效)
 *    需要导入jest的工具包(io.searchbox.client.jestClient)
 * 2、springData ElasticSearch(springboot自动注入的es版本可能过低)
 *     1) Client 节点信息clusterNodes;clusterName
 *     2) ElasticSearchTemplate操作ES
 *     3) 编写一个ElasticSearchRepository的子接口操作ES
 */
@MapperScan(value = "com.example.springbootdemo.base.mapper")
@SpringBootApplication
@EnableCaching
@EnableRabbit //开启基于注解的RabbitMQ
@EnableAsync //开启基于注解的异步任务支持
@EnableScheduling //开启基于注解的定时任务支持
public class SpringbootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDemoApplication.class, args);
    }
}
