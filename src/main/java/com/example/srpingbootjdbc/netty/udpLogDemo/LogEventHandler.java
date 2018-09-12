package com.example.srpingbootjdbc.netty.udpLogDemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Description: 处理 LogEvent 消息
 * @Author: chujunjie
 * @Date: Create in 12:20 2018/9/12
 * @Modified By
 */
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace(); //在异常时，输出消息并关闭 channel
        ctx.close();
    }

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, LogEvent event) throws Exception {
        StringBuilder builder = new StringBuilder(); //建立一个 StringBuilder 并构建输出
        builder.append(event.getReceivedTimestamp());
        builder.append(" [");
        builder.append(event.getSource().toString());
        builder.append("] [");
        builder.append(event.getLogfile());
        builder.append("] : ");
        builder.append(event.getMsg());

        System.out.println(builder.toString()); //打印出 LogEvent 的数据
    }
}