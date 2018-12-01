package com.example.srpingbootjdbc.io.iodemo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 1:19 2018/12/2
 * @Modified By
 */
public class MultiplexerTimeServer implements Runnable {
    private Selector selector;

    private ServerSocketChannel servChannel;

    private volatile boolean stop;

    /**
     * 初始化多路复用器、绑定监听端口
     *
     * @param port
     */
    public MultiplexerTimeServer(int port) {
        try {
            //创建selector
            selector = Selector.open();
            //创建channel
            servChannel = ServerSocketChannel.open();
            //设置非阻塞
            servChannel.configureBlocking(false);
            //绑定端口
            servChannel.socket().bind(new InetSocketAddress(port), 1024);
            //注册到selector接收监听
            servChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start in port : " + port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectedKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null)
                                key.channel().close();
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        //多路复用器关闭后，所有注册在上面的Channel和Pipe等资源都会被自动去注册并关闭，所以不需要重复释放资源
        if (selector != null)
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void handleInput(SelectionKey key) throws IOException {

        if (key.isValid()) {
            //处理新接入的请求消息
            if (key.isAcceptable()) {
                //接收新接入
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                //三次握手
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                //注册channel到selector
                sc.register(selector, SelectionKey.OP_READ);
            }
            if (key.isReadable()) {
                //读取数据
                SocketChannel sc = (SocketChannel) key.channel();
                //创建1MB的缓冲区
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0) {
                    //将缓冲区当前的limit设置为position，position为0，用于后续对缓冲区的读取操作
                    readBuffer.flip();
                    //根据缓冲区可读字节数创建字节数组
                    byte[] bytes = new byte[readBuffer.remaining()];
                    //将缓冲区的字节复制到新创建的字节数组中
                    readBuffer.get(bytes);
                    //构建字符串便于输出
                    String body = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println("The time server receive order : "
                            + body);
                    String currentTime = "QUERY TIME ORDER"
                            .equalsIgnoreCase(body) ? new java.util.Date(
                            System.currentTimeMillis()).toString()
                            : "BAD ORDER";
                    doWrite(sc, currentTime);
                } else if (readBytes < 0) {
                    // 对端链路关闭
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    private void doWrite(SocketChannel channel, String response) throws IOException {
        if (response != null && response.trim().length() > 0) {
            //字符串编码成字节数组
            byte[] bytes = response.getBytes();
            //创建缓冲区
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            //复制字节数组
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }
    }
}
