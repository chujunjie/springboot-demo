package com.example.springbootdemo.io.netty.nettySimpleDemo.initializer;

import io.netty.channel.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;

/**
 * @Description: 分块写入大型数据，支持异步写大数据流不引起高内存消耗
 * @Author: chujunjie
 * @Date: Create in 14:41 2018/9/11
 * @Modified By
 */
public class ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel> {
    private final File file;
    private final SslContext sslCtx;

    public ChunkedWriteHandlerInitializer(File file, SslContext sslCtx) {
        this.file = file;
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new SslHandler(sslCtx.newEngine(ch.alloc()))); //添加 SslHandler 到 ChannelPipeline
        pipeline.addLast(new ChunkedWriteHandler());//添加 ChunkedWriteHandler 用来处理作为 ChunkedInput 传进的数据
        pipeline.addLast(new WriteStreamHandler());//当连接建立时，WriteStreamHandler 开始写文件的内容
    }

    public final class WriteStreamHandler extends ChannelInboundHandlerAdapter {

        //当连接建立时，channelActive() 触发使用 ChunkedInput 来写文件的内容 (插图显示了 FileInputStream;也可以使用任何 InputStream )
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
        }
    }
}
