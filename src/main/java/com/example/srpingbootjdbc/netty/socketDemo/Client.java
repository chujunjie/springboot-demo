package com.example.srpingbootjdbc.netty.socketDemo;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @Description: 客户端
 * @Author: chujunjie
 * @Date: Create in 14:12 2018/9/7
 * @Modified By
 */
public class Client {
    public static void main(String[] args) {
        try {
            //1.创建客户端Socket，指定服务器地址和端口
            Socket socket = new Socket("localhost", 8888);

            //2.获取输出流，向服务器端发送信息
            OutputStream os = socket.getOutputStream();//字节输出流
            PrintWriter pw = new PrintWriter(os);//将输出流包装为打印流
            pw.write("用户名：admin;密码：123456");
            pw.flush();
            socket.shutdownOutput();//关闭输出流

            //3.获取输入流，并读取服务器端的响应信息
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String info = null;
            while ((info = br.readLine()) != null) {
                System.out.println("我是客户端，服务器说：" + info);
            }

            //4.关闭资源
            br.close();
            is.close();
            pw.close();
            os.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
