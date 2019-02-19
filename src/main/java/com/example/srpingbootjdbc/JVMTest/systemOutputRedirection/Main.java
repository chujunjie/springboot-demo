package com.example.srpingbootjdbc.JVMTest.systemOutputRedirection;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description: 测试类：修改class文件实现System标准输出重定向
 * @Author: chujunjie
 * @Date: Create in 8:36 2018/9/4
 * @Modified By
 */
public class Main {
    public static void main(String[] args) {
//        System.out.println("第一行");
//        System.out.println("第二行");
//        System.out.println("第三行");
        try {
            exec();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void exec() throws IOException {
        InputStream inputStream = new FileInputStream("C:/Users/lenovo/Main.class");
        byte[] classBytes = new byte[inputStream.available()];
        inputStream.read(classBytes);
        inputStream.close();
        //偷梁换柱
        ClassModifier classModifier = new ClassModifier(classBytes);
        classBytes = classModifier.modifyUTF8Constant("java/lang/System", "com/example/srpingbootjdbc/JVMTest/HackSystem");
        //输出查看
        OutputStream outputStream = new FileOutputStream("C:/Users/lenovo/Main2.class");
        outputStream.write(classBytes);
        outputStream.flush();
        outputStream.close();
        //
        HotSwapClassLoader loader = new HotSwapClassLoader();
        Class clazz = loader.loadByte(classBytes);
        try {
            Method method = clazz.getMethod("main", String[].class);
            method.invoke(null, (Object) new String[]{null});
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
