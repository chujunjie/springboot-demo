package com.example.srpingbootjdbc.JVMTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 运行时常量池内存溢出异常（JDK1.6及之前） 配置参数：-XX:PermSize=10M -XX:MaxPermSize=10M
 * @Author: chujunjie
 * @Date: Create in 14:42 2018/8/27
 * @Modified By
 */
public class RuntimeConstantPoolOOM {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        int i = 0;
        for (;;) {
            /*
            intern()是一个Native方法，如果常量池中已经包含一个等于此String对象的字符串，则返回对象
            否则将String对象包含的字符串添加到常量池，并返回String对象的引用
             */
            list.add(String.valueOf(i).intern());
        }
    }
}
