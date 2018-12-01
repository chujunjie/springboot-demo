package com.example.srpingbootjdbc.io.netty.nettySimpleDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @Description: 定义处理入站事件的方法
 * @Author: chujunjie
 * @Date: Create in 11:13 2018/9/10
 * @Modified By
 */
@ChannelHandler.Sharable //标识这类的实例之间可以在 channel 里面共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 信息入站时调用，覆盖channelRead() 方法处理进来的数据用来响应释放资源
     * 可以使用ReferenceCountUtil.release() 来丢弃收到的信息，write()方法已经包含，可以通过使用 SimpleChannelInboundHandler 简化资源管理问题
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx,
                            Object msg) {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8)); //日志消息输出到控制台
        ctx.write(in); //将所接收的消息返回给发送者
    }

    /**
     * 通知处理器最后的 channelread() 是当前批处理中的最后一条消息时调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER) //冲刷所有待审消息到远程节点。关闭通道后，操作完成
                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 读操作时捕获到异常时调用
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        cause.printStackTrace(); //打印异常堆栈跟踪
        ctx.close(); //关闭通道
    }
}
