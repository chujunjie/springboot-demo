package com.example.springbootdemo.jvm.systemOutputRedirection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * @Description: 输出类
 * @Author: chujunjie
 * @Date: Create in 8:34 2018/9/4
 * @Modified By
 */
public class MyPrintStream extends PrintStream {

    private int i = 0;

    public MyPrintStream(File file) throws FileNotFoundException {
        super(file);
    }

    @Override
    public void println(String string) {
        super.println((++i) + "." + string);
    }
}
