package com.example.springbootdemo.io.netty.nettySimpleDemo.initializer;

import com.google.protobuf.MessageLite;
import io.netty.channel.*;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

/**
 * @Description: Google Protobuf序列化，编解码数据更加紧凑和高效。绑定各种编程语言,更适合跨语言项目。
 * @Author: chujunjie
 * @Date: Create in 14:48 2018/9/11
 * @Modified By
 */
public class ProtoBufInitializer extends ChannelInitializer<Channel> {

    private final MessageLite lite;

    public ProtoBufInitializer(MessageLite lite) {
        this.lite = lite;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ProtobufVarint32FrameDecoder()); //添加 ProtobufVarint32FrameDecoder 用来分割帧
        pipeline.addLast(new ProtobufEncoder()); //添加 ProtobufEncoder 用来处理消息的编码
        pipeline.addLast(new ProtobufDecoder(lite)); //添加 ProtobufDecoder 用来处理消息的解码
        pipeline.addLast(new ObjectHandler()); //添加 ObjectHandler 用来处理解码了的消息
    }

    public static final class ObjectHandler extends SimpleChannelInboundHandler<Object> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            // Do something with the object
        }
    }
}