package com.example.srpingbootjdbc.netty.udpLogDemo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @Description: 解码器
 * @Author: chujunjie
 * @Date: Create in 12:19 2018/9/12
 * @Modified By
 */
public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket datagramPacket, List<Object> out) throws Exception {
        ByteBuf data = datagramPacket.content(); //获取 DatagramPacket 中数据的引用
        int i = data.indexOf(0, data.readableBytes(), LogEvent.SEPARATOR);  //获取 SEPARATOR 的索引
        String filename = data.slice(0, i).toString(CharsetUtil.UTF_8);  //从数据中读取文件名
        String logMsg =  data.slice(i + 1, data.readableBytes()).toString(CharsetUtil.UTF_8);  //读取数据中的日志消息

        LogEvent event = new LogEvent(datagramPacket.recipient(), System.currentTimeMillis(),
                filename,logMsg); //构造新的 LogEvent 对象并将其添加到列表中
        out.add(event);
    }
}
