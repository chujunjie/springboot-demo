package com.example.srpingbootjdbc.io.netty.nettySimpleDemo;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * @Description: 自定义编解码器
 *               CombinedByteCharCodec 的参数是解码器和编码器的实现用于处理进站字节和出站消息
 * @Author: chujunjie
 * @Date: Create in 11:05 2018/9/11
 * @Modified By
 */
public class CombinedByteCharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {

    public CombinedByteCharCodec() {
        super(new ByteToCharDecoder(), new CharToByteEncoder()); //传递 ByteToCharDecoder 和 CharToByteEncoder 实例到 super 构造函数来委托调用使他们结合起来
    }
}
