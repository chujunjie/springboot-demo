package com.example.springbootdemo.io.netty.udpLogDemo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

/**
 * @Description: 服务器
 * @Author: chujunjie
 * @Date: Create in 11:33 2018/9/12
 * @Modified By
 */
public class LogEventBroadcaster {
    private final Bootstrap bootstrap;
    private final File file;
    private final EventLoopGroup group;

    public LogEventBroadcaster(InetSocketAddress address, File file) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new LogEventEncoder(address)); //引导 NioDatagramChannel 。为了使用广播，我们设置 SO_BROADCAST 的 socket 选项

        this.file = file;
    }

    public void run() throws IOException {
        Channel ch = bootstrap.bind(0).syncUninterruptibly().channel(); //绑定管道。注意当使用 Datagram Channel 时，是没有连接的
        System.out.println("LogEventBroadcaster running");
        long pointer = 0;
        for (;;) {
            long len = file.length();
            if (len < pointer) {
                // file was reset
                pointer = len; //如果需要，可以设置文件的指针指向文件的最后字节
            } else if (len > pointer) {
                // Content was added
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                raf.seek(pointer);  //设置当前文件的指针，这样不会把旧的发出去
                String line;
                while ((line = raf.readLine()) != null) {
                    ch.writeAndFlush(new LogEvent(null, -1, file.getAbsolutePath(), line));  //写一个 LogEvent 到管道用于保存文件名和文件实体。(期望每个日志实体是一行长度)
                }
                pointer = raf.getFilePointer(); //存储当前文件的位置，这样，我们可以稍后继续
                raf.close();
            }
            try {
                Thread.sleep(1000);  //睡 1 秒。如果其他中断退出循环就重新启动它
            } catch (InterruptedException e) {
                Thread.interrupted();
                break;
            }
        }
    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
//        if (args.length != 2) {
//            throw new IllegalArgumentException();
//        }

        LogEventBroadcaster broadcaster = new LogEventBroadcaster(new InetSocketAddress("255.255.255.255",
                Integer.parseInt("8081")), new File("C://Users/lenovo/Desktop/hello.log"));  //构造一个新的实例 LogEventBroadcaster 并启动它
        try {
            broadcaster.run();
        } finally {
            broadcaster.stop();
        }
    }
}
