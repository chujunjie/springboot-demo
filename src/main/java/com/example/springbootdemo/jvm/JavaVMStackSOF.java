package com.example.springbootdemo.jvm;

/**
 * @Description: 栈溢出  配置参数：-Xss128k
 * @Author: chujunjie
 * @Date: Create in 14:28 2018/8/27
 * @Modified By
 */
public class JavaVMStackSOF {

    private int stackLength = 1;

    private void stackLeak() {
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) {
        JavaVMStackSOF sof = new JavaVMStackSOF();
        try {
            sof.stackLeak();
        } catch (Throwable e) {
            System.out.println("stack length:" + sof.stackLength);
            throw e;
        }
    }
}
