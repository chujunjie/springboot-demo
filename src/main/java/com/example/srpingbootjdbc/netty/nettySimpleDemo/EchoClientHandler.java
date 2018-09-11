package com.example.srpingbootjdbc.netty.nettySimpleDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 13:12 2018/9/10
 * @Modified By
 */
@ChannelHandler.Sharable //@Sharable标记这个类的实例可以在 channel 里共享
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8)); //当被通知该 channel 是活动的时候就发送信息
    }

    /**
     * 使用channelRead0会自动管理资源，而无需存储任何信息的引用
     * @param ctx
     * @param in
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8));    //记录接收到的消息
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {     //记录日志错误并关闭 channel
        cause.printStackTrace();
        ctx.close();
    }
}
