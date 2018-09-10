package com.example.srpingbootjdbc.netty.nettySimpleDemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Description: EchoServer服务端
 * @Author: chujunjie
 * @Date: Create in 12:53 2018/9/10
 * @Modified By
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
//        if (args.length != 1) {
//            System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
//            return;
//        }
        int port = Integer.parseInt("8081"); //设置端口值（抛出一个 NumberFormatException 如果该端口参数的格式不正确）
        new EchoServer(port).start(); //呼叫服务器的 start() 方法
    }

    public void start() throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup(); //创建 EventLoopGroup
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)                                //创建 ServerBootstrap
                    .channel(NioServerSocketChannel.class)        //指定使用 NIO 的传输 Channel
                    .localAddress(new InetSocketAddress(port))    //设置 socket 地址使用所选的端口
                    .childHandler(new ChannelInitializer<SocketChannel>() {  //添加 EchoServerHandler 到 Channel 的 ChannelPipeline
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });

            ChannelFuture f = b.bind().sync();            //绑定的服务器;sync 等待服务器关闭
            System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());

            f.addListener((ChannelFutureListener) channelFuture -> { //新增
                if (channelFuture.isSuccess()) {
                    System.out.println("Write successful");
                } else {
                    System.err.println("Write error");
                    channelFuture.cause().printStackTrace();
                }
            });

            f.channel().closeFuture().sync();            //关闭 channel 和 块，直到它被关闭
        } finally {
            group.shutdownGracefully().sync();            //关机的 EventLoopGroup，释放所有资源
        }
    }

}
