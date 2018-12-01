package com.example.srpingbootjdbc.io.netty.chatDemo;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @Description: 处理 HTTP 请求
 * @Author: chujunjie
 * @Date: Create in 8:50 2018/9/12
 * @Modified By
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String wsUri;
    private static final File INDEX;

    static {
        URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            String path = location.toURI() + "index.html";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to locate index.html", e);
        }
    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    public void channelRead0(ChannelHandlerContext context, FullHttpRequest request) throws Exception {
        if (wsUri.equalsIgnoreCase(request.uri())) {
            context.fireChannelRead(request.retain()); //如果请求是一次升级了的 WebSocket 请求，则递增引用计数器（retain）并且将它传递给在 ChannelPipeline 中的下个 ChannelInboundHandler
        } else {
            if (HttpUtil.is100ContinueExpected(request)) {
                send100Continue(context);  //处理符合 HTTP 1.1的 "100 Continue" 请求
            }
        }

        RandomAccessFile file = new RandomAccessFile(INDEX, "r"); //读取 index.html

        HttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");

        boolean keepAlive = HttpUtil.isKeepAlive(request);

        if (keepAlive) { //判断 keepAlive 是否在请求头里面
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        context.write(response); //写 HttpResponse 到客户端

        if (context.pipeline().get(SslHandler.class) == null) {  //写 index.html 到客户端，根据 ChannelPipeline 中是否有 SslHandler 来决定使用 DefaultFileRegion 还是 ChunkedNioFile
            context.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
        } else {
            context.write(new ChunkedNioFile(file.getChannel()));
        }
        ChannelFuture future = context.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);  //写并刷新 LastHttpContent 到客户端，标记响应完成
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE); //如果 请求头中不包含 keepAlive，当写完成时，关闭 Channel
        }
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
