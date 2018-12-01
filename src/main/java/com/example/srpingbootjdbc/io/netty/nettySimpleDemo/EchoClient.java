package com.example.srpingbootjdbc.io.netty.nettySimpleDemo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Description: EchoClient 客户端
 * @Author: chujunjie
 * @Date: Create in 13:15 2018/9/10
 * @Modified By
 */
public class EchoClient {

    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();                //创建 Bootstrap
            b.group(group)                                //指定 EventLoopGroup 来处理客户端事件。由于使用 NIO 传输，所以用 NioEventLoopGroup 的实现
                    .channel(NioSocketChannel.class)            //使用的 channel 类型是一个用于 NIO 传输
                    .remoteAddress(new InetSocketAddress(host, port))    //设置服务器的 InetSocketAddress
                    .handler(new ChannelInitializer<SocketChannel>() {    //当建立一个连接和一个新的通道时，创建添加到 EchoClientHandler 实例 到 channel pipeline
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline(); //ChannelPipeline 提供了一个容器给 ChannelHandler 链并提供了一个API 用于管理沿着链入站和出站事件的流动
                            pipeline.addLast(new EchoClientHandler()); //事件处理链
                        }
                    });

            ChannelFuture f = b.connect().sync();        //连接到远程;等待连接完成

            f.channel().closeFuture().sync();            //阻塞直到 Channel 关闭
        } finally {
            group.shutdownGracefully().sync();            //调用 shutdownGracefully() 来关闭线程池和释放所有资源
        }
    }

    public static void main(String[] args) throws Exception {
//        if (args.length != 2) {
//            System.err.println("Usage: " + EchoClient.class.getSimpleName() + " <host> <port>");
//            return;
//        }

        final String host = "localhost";
        final int port = Integer.parseInt("8081");

        new EchoClient(host, port).start();
    }
}
