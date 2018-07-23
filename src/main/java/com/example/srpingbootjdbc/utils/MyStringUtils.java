package com.example.srpingbootjdbc.utils;

public class MyStringUtils {

    public static boolean equals(String str1,String str2){
        if (str1 == null || "".equals(str1)){
            if (str2 == null || "".equals(str2)){
                return true;
            }
            return false;
        } else {
            if (str2 == null || "".equals(str2)){
                return false;
            }else {
                if (str1.equals(str2)){
                    return true;
                }
                return false;
            }
        }
    }
}
