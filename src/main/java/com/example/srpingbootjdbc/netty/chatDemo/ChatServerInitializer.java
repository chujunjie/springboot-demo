package com.example.srpingbootjdbc.netty.chatDemo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 9:59 2018/9/12
 * @Modified By
 */
public class ChatServerInitializer extends ChannelInitializer<Channel> {
    private final ChannelGroup group;

    public ChatServerInitializer(ChannelGroup group) {
        this.group = group;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception { //添加 ChannelHandler　到 ChannelPipeline
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec()); //字节解码
        pipeline.addLast(new HttpObjectAggregator(64 * 1024)); //http消息聚合
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpRequestHandler("/ws"));
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws")); //处理WebSocket升级握手
        pipeline.addLast(new TextWebSocketFrameHandler(group)); //处理TextWebSocketFrames和握手完成事件
    }
}
