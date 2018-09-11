package com.example.srpingbootjdbc.netty.ioPropagationMode.unicast;

import java.io.IOException;
import java.net.*;

/**
 * @Description: 单播客户端
 * @Author: chujunjie
 * @Date: Create in 14:56 2018/9/7
 * @Modified By
 */
public class UnicastClient {
    private static final int MAXRECEIVED = 255;

    public static void main(String[] args) throws IOException {
        byte[] msg = new String("connect test successfully!!!").getBytes();

        DatagramSocket client = new DatagramSocket();

        InetAddress inetAddr = InetAddress.getLocalHost();
        SocketAddress socketAddr = new InetSocketAddress(inetAddr, 8888);

        DatagramPacket sendPacket = new DatagramPacket(msg, msg.length,
                socketAddr);

        client.send(sendPacket);

        client.close();
    }
}

