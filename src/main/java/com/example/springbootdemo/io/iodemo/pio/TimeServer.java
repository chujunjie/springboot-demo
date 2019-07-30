package com.example.springbootdemo.io.iodemo.pio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description: 伪异步I/O
 * @Author: chujunjie
 * @Date: Create in 1:04 2018/12/2
 * @Modified By
 */
public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port : " + port);
            Socket socket = null;
            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(
                    50, 10000);// 创建IO任务线程池
            while (true) {
                socket = server.accept();
                singleExecutor.execute(new TimeServerHandler(socket));
            }
        } finally {
            if (server != null) {
                System.out.println("The time server close");
                server.close();
                server = null;
            }
        }
    }
}
