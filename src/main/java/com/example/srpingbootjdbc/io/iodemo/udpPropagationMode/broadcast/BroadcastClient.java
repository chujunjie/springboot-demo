package com.example.srpingbootjdbc.io.iodemo.udpPropagationMode.broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @Description: 广播客户端
 * @Author: chujunjie
 * @Date: Create in 15:03 2018/9/7
 * @Modified By
 */
public class BroadcastClient {
    public static void main(String[] args) throws IOException {
        byte[] msg = new String("connection successfully!!!").getBytes();

        InetAddress inetAddr = InetAddress.getByName("255.255.255.255");
        DatagramSocket client = new DatagramSocket();

        DatagramPacket sendPack = new DatagramPacket(msg, msg.length, inetAddr,
                8888);

        client.send(sendPack);
        System.out.println("Client send msg complete");
        client.close();
    }
}
