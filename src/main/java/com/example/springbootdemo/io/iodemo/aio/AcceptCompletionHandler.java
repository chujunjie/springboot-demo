package com.example.springbootdemo.io.iodemo.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 1:37 2018/12/2
 * @Modified By
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler> {

    @Override
    public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
        /*
         再次调用accept方法，如果有新的客户端连接接入，系统将回调传入的CompletionHandler实例的
         completed方法，表示新的客户端已经成功接入。
         AsynchronousSocketChannel能接收多个客户端，所以需要继续调用accept方法
         */
        attachment.asynchronousServerSocketChannel.accept(attachment, this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        /*
        buffer:接收缓冲区，用于从异步Channel中读取数据
        attachment:异步Channel携带的附件，通知回调时做入参
        CompletionHandler<Integer, ByteBuffer>：接收通知回调的业务Handler
         */
        result.read(buffer, buffer, new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
        exc.printStackTrace();
        attachment.latch.countDown();
    }
}
