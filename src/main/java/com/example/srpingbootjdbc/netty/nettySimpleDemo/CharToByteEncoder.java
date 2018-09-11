package com.example.srpingbootjdbc.netty.nettySimpleDemo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Description: 编码器，继承自 MessageToByteEncoder，编码 char 消息 到 ByteBuf
 * @Author: chujunjie
 * @Date: Create in 11:02 2018/9/11
 * @Modified By
 */
public class CharToByteEncoder extends MessageToByteEncoder<Character> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Character character, ByteBuf byteBuf) throws Exception {
        byteBuf.writeByte(character); //写 char 到 ByteBuf
    }
}
