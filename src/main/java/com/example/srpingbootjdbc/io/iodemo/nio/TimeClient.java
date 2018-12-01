package com.example.srpingbootjdbc.io.iodemo.nio;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 1:26 2018/12/2
 * @Modified By
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        new Thread(new TimeClientHandler("127.0.0.1", port), "TimeClient-001")
                .start();
    }
}
