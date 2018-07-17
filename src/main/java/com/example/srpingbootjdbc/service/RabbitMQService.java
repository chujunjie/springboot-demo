package com.example.srpingbootjdbc.service;

import com.example.srpingbootjdbc.bean.Employee;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {

    @RabbitListener(queues = "atguigu.news")
    public void receive(Employee employee){
        System.out.println("收到消息："+employee.toString());
    }
}
