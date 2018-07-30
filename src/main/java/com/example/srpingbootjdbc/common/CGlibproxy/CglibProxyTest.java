package com.example.srpingbootjdbc.common.CGlibproxy;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 15:11 2018/7/27
 * @Modified By
 */
public class CglibProxyTest {
    public static void main(String[] args) {

        BookFacadeImpl bookFacade=new BookFacadeImpl();
        BookFacadeCglib  cglib=new BookFacadeCglib();
        BookFacadeImpl bookCglib=(BookFacadeImpl)cglib.getInstance(bookFacade);
        bookCglib.addBook();
    }

}
