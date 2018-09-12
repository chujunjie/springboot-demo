package com.example.srpingbootjdbc.netty.udpLogDemo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * @Description: 监视器
 * @Author: chujunjie
 * @Date: Create in 12:21 2018/9/12
 * @Modified By
 */
public class LogEventMonitor {

    private final Bootstrap bootstrap;
    private final EventLoopGroup group;
    public LogEventMonitor(InetSocketAddress address) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)  //引导 NioDatagramChannel。设置 SO_BROADCAST socket 选项
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new LogEventDecoder());  //添加 ChannelHandler 到 ChannelPipeline
                        pipeline.addLast(new LogEventHandler());
                    }
                }).localAddress(address);

    }

    public Channel bind() {
        return bootstrap.bind().syncUninterruptibly().channel();  //绑定的通道。注意,在使用 DatagramChannel 是没有连接，因为这些 无连接
    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
//        if (args.length != 1) {
//            throw new IllegalArgumentException("Usage: LogEventMonitor <port>");
//        }
        LogEventMonitor monitor = new LogEventMonitor(new InetSocketAddress(Integer.parseInt("8080")));  //构建一个新的 LogEventMonitor
        try {
            Channel channel = monitor.bind();
            System.out.println("LogEventMonitor running");

            channel.closeFuture().await();
        } finally {
            monitor.stop();
        }
    }
}
