package com.example.springbootdemo.io.netty.udpLogDemo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Description: 编码器
 * @Author: chujunjie
 * @Date: Create in 11:28 2018/9/12
 * @Modified By
 */
public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {
    private final InetSocketAddress remoteAddress;

    public LogEventEncoder(InetSocketAddress remoteAddress) {  //LogEventEncoder 创建了 DatagramPacket 消息类发送到指定的 InetSocketAddress
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, LogEvent logEvent, List<Object> out) throws Exception {
        byte[] file = logEvent.getLogfile().getBytes(CharsetUtil.UTF_8); //写文件名到 ByteBuf
        byte[] msg = logEvent.getMsg().getBytes(CharsetUtil.UTF_8);
        ByteBuf buf = channelHandlerContext.alloc().buffer(file.length + msg.length + 1);
        buf.writeBytes(file);
        buf.writeByte(LogEvent.SEPARATOR); //添加一个 SEPARATOR
        buf.writeBytes(msg);  //写一个日志消息到 ByteBuf
        out.add(new DatagramPacket(buf, remoteAddress));  //添加新的 DatagramPacket 到出站消息
    }
}
