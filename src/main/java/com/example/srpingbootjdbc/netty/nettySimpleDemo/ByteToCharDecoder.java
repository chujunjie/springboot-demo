package com.example.srpingbootjdbc.netty.nettySimpleDemo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Description: 解码器，实现扩展 ByteToMessageDecoder ，从 ByteBuf 读取字符
 * @Author: chujunjie
 * @Date: Create in 10:59 2018/9/11
 * @Modified By
 */
public class ByteToCharDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() >= 2) { //写 char 到 MessageBuf
            list.add(byteBuf.readChar());
        }
    }
}
