package com.example.springbootdemo.io.netty.nettySimpleDemo.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @Description: 启用HTTPS协议，运用SSL/TLS加密
 *               在大多数情况下,SslHandler 将成为 ChannelPipeline 中的第一个 ChannelHandler 。
 *               这将确保所有其他 ChannelHandler 应用他们的逻辑到数据后加密后才发生,从而确保他们的变化是安全的。
 * @Author: chujunjie
 * @Date: Create in 11:17 2018/9/11
 * @Modified By
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {

    private final SslContext context;
    private final boolean startTls;
    private final boolean client;

    public SslChannelInitializer(SslContext context, boolean startTls, boolean client) { //使用构造函数来传递 SSLContext 用于使用(startTls 是否启用)
        this.context = context;
        this.startTls = startTls;
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        SSLEngine engine = context.newEngine(ch.alloc());  //从 SslContext 获得一个新的 SslEngine 。给每个 SslHandler 实例使用一个新的 SslEngine
        engine.setUseClientMode(true); //设置 SslEngine 是 client 或者是 server 模式
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addFirst("ssl", new SslHandler(engine, startTls));  //添加 SslHandler 到 pipeline 作为第一个处理器以启用 HTTPS

        if (client) {
            pipeline.addLast("codec", new HttpClientCodec()); //添加 HttpClientCodec
        } else {
            pipeline.addLast("codec", new HttpServerCodec());  //Server模式：添加 HttpServerCodec
        }
    }
}
