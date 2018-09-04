package com.example.srpingbootjdbc.JVMTest.systemOutputRedirection;

/**
 * @Description: 为了多次载入执行类而加入的加载器
 *               把defineClass方法公开出来，只有外部显示调用的时候才会使用到loadByte方法
 *               由虚拟机调用时，仍然按照原有的双亲委派规则使用loadClass方法进行加载
 * @Author: chujunjie
 * @Date: Create in 16:18 2018/9/3
 * @Modified By
 */
public class HotSwapClassLoader extends ClassLoader {

    public HotSwapClassLoader() {
        super(HotSwapClassLoader.class.getClassLoader());
    }

    public Class loadByte(byte[] classByte) {
        return defineClass(null, classByte, 0, classByte.length);
    }
}
