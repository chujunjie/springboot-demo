package com.example.srpingbootjdbc.JVMTest.systemOutputRedirection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * @Description: 为JavaClass劫持java.lang.System提供支持
 *               除了out和err外，其余的都直接转发给System处理
 * @Author: chujunjie
 * @Date: Create in 8:44 2018/9/4
 * @Modified By
 */
public class HackSystem {

    public static PrintStream out;

    static {
        try {
            out = new MyPrintStream(new File("C:/Users/lenovo/out.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
