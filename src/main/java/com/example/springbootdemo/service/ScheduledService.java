package com.example.springbootdemo.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledService {

    /**
     * second(秒)，minute(分)，hour(时)，day of month(日)，month（月），day of week(周几)
     * 0 * * * * MON-FRI
     */
    @Scheduled(cron = "0 * * * * MON-FRI")
    public void hello(){
        System.out.println("hello world....");
    }
}
