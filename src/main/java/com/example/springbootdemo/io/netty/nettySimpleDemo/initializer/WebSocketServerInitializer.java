package com.example.springbootdemo.io.netty.nettySimpleDemo.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * @Description: WebSocket
 *               WebSocket 允许数据双向传输，而不需要请求-响应模式。
 *               提供一个 TCP 连接两个方向的交通。结合 WebSocket API 它提供了一个替代 HTTP 轮询双向通信从页面到远程服务器。
 *               提供真正的双向客户机和服务器之间的数据交换。
 * @Author: chujunjie
 * @Date: Create in 14:03 2018/9/11
 * @Modified By
 */
public class WebSocketServerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(
                new HttpServerCodec(),
                new HttpObjectAggregator(65536),  //添加 HttpObjectAggregator 用于提供在握手时聚合 HttpRequest
                new WebSocketServerProtocolHandler("/websocket"),  //添加 WebSocketServerProtocolHandler 用于处理握手如果请求是发送到"/websocket." 端点，当升级完成后，它将会处理Ping, Pong 和 Close 帧
                new TextFrameHandler(),  //TextFrameHandler 将会处理 TextWebSocketFrames
                new BinaryFrameHandler(),  //BinaryFrameHandler 将会处理 BinaryWebSocketFrames
                new ContinuationFrameHandler());  //ContinuationFrameHandler 将会处理ContinuationWebSocketFrames
    }

    /**
     * 文本处理
     */
    public static final class TextFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
            // TODO Handle text frame
        }
    }

    /**
     * 二进制处理
     */
    public static final class BinaryFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {
            // TODO Handle binary frame
        }
    }

    /**
     * 延迟处理
     */
    public static final class ContinuationFrameHandler extends SimpleChannelInboundHandler<ContinuationWebSocketFrame> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, ContinuationWebSocketFrame msg) throws Exception {
            // TODO Handle continuation frame
        }
    }
}
