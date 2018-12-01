package com.example.srpingbootjdbc.io.iodemo.udpPropagationMode.unicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

/**
 * @Description: 单播服务端
 * @Author: chujunjie
 * @Date: Create in 15:00 2018/9/7
 * @Modified By
 */
class UnicastServer {
    private static final int MAXREV = 255;

    public static void main(String[] args) throws IOException {
        DatagramSocket server = new DatagramSocket(8888);
        DatagramPacket recvPacket = new DatagramPacket(new byte[MAXREV], MAXREV);

        for (;;) {
            server.receive(recvPacket);

            byte[] receiveMsg = Arrays.copyOfRange(recvPacket.getData(),
                    recvPacket.getOffset(),
                    recvPacket.getOffset() + recvPacket.getLength());

            System.out.println("Handing at client "
                    + recvPacket.getAddress().getHostName() + " ip "
                    + recvPacket.getAddress().getHostAddress());

            System.out.println("UnicastServer Receive Data:" + new String(receiveMsg));

            server.send(recvPacket);

        }

    }
}
