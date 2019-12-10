package com.example.springbootdemo.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Java堆溢出   配置参数：-Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError（出现内存溢出时Dump出当前的内存堆转储快照以便事后进行分析）
 * @Author: chujunjie
 * @Date: Create in 14:12 2018/8/27
 * @Modified By
 */
public class HeapOOM {

    private static class OOMObject {
    }

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        for (;;) {
            list.add(new OOMObject());
        }
    }
}
