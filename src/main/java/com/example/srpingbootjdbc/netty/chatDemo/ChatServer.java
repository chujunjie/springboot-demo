package com.example.srpingbootjdbc.netty.chatDemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

import java.net.InetSocketAddress;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 10:06 2018/9/12
 * @Modified By
 */
public class ChatServer {

    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);//创建 DefaultChannelGroup 用来保存所有连接的的 WebSocket channel
    private final EventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;

    public ChannelFuture start(InetSocketAddress address) {
        ServerBootstrap b = new ServerBootstrap(); //引导服务器
        b.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(createInitializer(channelGroup));
        ChannelFuture future = b.bind(address).syncUninterruptibly();
        channel = future.channel();
        return future;
    }

    protected ChannelInitializer<Channel> createInitializer(ChannelGroup group) { //创建 ChannelInitializer
        return new ChatServerInitializer(group);
    }

    public void destroy() {        //处理服务器关闭，包括释放所有资源
        if (channel != null) {
            channel.close();
        }
        channelGroup.close();
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception{
//        if (args.length != 1) {
//            System.err.println("Please give port as argument");
//            System.exit(1);
//        }
        int port = Integer.parseInt("8081");

        final ChatServer endpoint = new ChatServer();
        ChannelFuture future = endpoint.start(new InetSocketAddress(port));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> endpoint.destroy()));
        future.channel().closeFuture().syncUninterruptibly();
    }
}
