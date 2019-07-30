package com.example.springbootdemo.io.iodemo.aio;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 1:39 2018/12/2
 * @Modified By
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        //通过独立I/O线程创建异步时间服务器客户端Handler，在实际项目中不需要创建
        new Thread(new AsyncTimeClientHandler("127.0.0.1", port),
                "AIO-AsyncTimeClientHandler-001").start();
    }
}
