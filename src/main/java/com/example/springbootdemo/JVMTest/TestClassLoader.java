package com.example.springbootdemo.JVMTest;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description: 类加载器测试：比较两个类是否相等，只有在两个类是由同一个类加载器加载的前提下才有意义
 *                            否则，即使两个类来源于同一个Class文件，被同一个虚拟机加载，类加载器不同，则必然不相等
 * @Author: chujunjie
 * @Date: Create in 16:26 2018/8/31
 * @Modified By
 */
public class TestClassLoader {

    public static void main(String[] args) throws Exception {
        ClassLoader myLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                try {
                    String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                    InputStream is = getClass().getResourceAsStream(fileName);
                    if (is == null) {
                        return super.loadClass(name);
                    }
                    byte[] bytes = new byte[is.available()];
                    is.read(bytes);
                    return defineClass(name, bytes, 0, bytes.length);
                } catch (IOException e) {
                    throw new ClassNotFoundException(name);
                }
            }
        };

        Object obj = myLoader.loadClass("com.example.springbootdemo.JVMTest.TestClassLoader").newInstance();

        System.out.println(obj.getClass());
        System.out.println(obj instanceof TestClassLoader);
    }
}
