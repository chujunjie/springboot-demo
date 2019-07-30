package com.example.springbootdemo.io.netty.udpLogDemo;

import java.net.InetSocketAddress;

/**
 * @Description: 消息事件单元
 * @Author: chujunjie
 * @Date: Create in 11:29 2018/9/12
 * @Modified By
 */
public final class LogEvent {
    public static final byte SEPARATOR = (byte) ':';

    private final InetSocketAddress source;
    private final String logfile;
    private final String msg;
    private final long received;

    public LogEvent(String logfile, String msg) { //构造器用于出站消息
        this(null, -1, logfile, msg);
    }

    public LogEvent(InetSocketAddress source, long received, String logfile, String msg) {  //构造器用于入站消息
        this.source = source;
        this.logfile = logfile;
        this.msg = msg;
        this.received = received;
    }

    public InetSocketAddress getSource() { //返回发送 LogEvent 的 InetSocketAddress 的资源
        return source;
    }

    public String getLogfile() { //返回用于发送 LogEvent 的日志文件的名称
        return logfile;
    }

    public String getMsg() {  //返回消息的内容
        return msg;
    }

    public long getReceivedTimestamp() {  //返回 LogEvent 接收到的时间
        return received;
    }
}
