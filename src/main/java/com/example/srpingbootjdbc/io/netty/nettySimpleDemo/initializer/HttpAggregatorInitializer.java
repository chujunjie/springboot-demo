package com.example.srpingbootjdbc.io.netty.nettySimpleDemo.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;

/**
 * @Description: HTTP消息聚合
 *               这个操作有一个轻微的成本,消息段需要缓冲,直到完全可以将消息转发到下一个 ChannelInboundHandler 管道。
 *               但好处是,不必担心消息碎片,总是能够看到完整的消息内容。
 *
 *               使用 HTTP 时建议压缩数据以减少传输流量，但是压缩数据会增加 CPU 负载。
 * @Author: chujunjie
 * @Date: Create in 13:41 2018/9/11
 * @Modified By
 */
public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {

    private final boolean client;

    public HttpAggregatorInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (client) {
            pipeline.addLast("codec", new HttpClientCodec());  //client: 添加 HttpClientCodec
            pipeline.addLast("decompressor",new HttpContentDecompressor());  //添加 HttpContentDecompressor 用于处理来自服务器的压缩的内容
        } else {
            pipeline.addLast("codec", new HttpServerCodec());  //server: server 模式时添加 HttpServerCodec
            pipeline.addLast("compressor",new HttpContentCompressor()); //HttpContentCompressor 用于压缩来自 client 支持的 HttpContentCompressor
        }
        pipeline.addLast("aggregator", new HttpObjectAggregator(512 * 1024));  //添加 HttpObjectAggregator 到 ChannelPipeline, 使用最大消息值是 512kb
    }
}
