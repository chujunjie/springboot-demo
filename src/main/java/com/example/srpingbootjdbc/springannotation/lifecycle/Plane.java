package com.example.srpingbootjdbc.springannotation.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 22:21 2019/2/15
 * @Modified By
 */
public class Plane implements InitializingBean, DisposableBean {

    public Plane() {
        System.out.println("plane constructor ...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("plane init ...");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("plane destroy ...");
    }
}
