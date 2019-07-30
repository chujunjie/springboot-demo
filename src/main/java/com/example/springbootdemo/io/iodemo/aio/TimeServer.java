package com.example.springbootdemo.io.iodemo.aio;

import java.io.IOException;

/**
 * @Description: AIO
 * @Author: chujunjie
 * @Date: Create in 1:36 2018/12/2
 * @Modified By
 */
public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
        new Thread(timeServer, "AIO-AsyncTimeServerHandler-001").start();
    }
}
