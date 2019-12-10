package com.example.springbootdemo.spring.proxy.CGlibproxy;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 15:11 2018/7/27
 * @Modified By
 */
public class CglibProxyTest {
    public static void main(String[] args) {
        BookFacade bookFacade = new BookFacade();
        BookFacadeCglib cglib = new BookFacadeCglib();
        BookFacade bookCglib = (BookFacade) cglib.getInstance(bookFacade);
        bookCglib.addBook();
    }

}
