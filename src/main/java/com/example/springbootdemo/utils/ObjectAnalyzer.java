package com.example.springbootdemo.utils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * 通用toString方法
 */
public class ObjectAnalyzer {

    private static ArrayList<Object> visited = new ArrayList<>();

    public static String toString(Object obj) {
        if (obj == null) return "null";
        if (visited.contains(obj)) return "...";
        visited.add(obj);
        Class<?> cl = obj.getClass();
        if (cl == String.class) return (String) obj;
        if (cl.isArray()) {
            StringBuilder r = new StringBuilder(cl.getComponentType() + "[]{");
            for (int i = 0; i < Array.getLength(obj); i++) {
                if (i > 0) r.append(",");
                Object val = Array.get(obj, i);
                if (cl.getComponentType().isPrimitive()) r.append(val);
                else r.append(toString(val));
            }
            return r + "}";
        }
        StringBuilder r = new StringBuilder(cl.getSimpleName());
        //检查这个类和所有父类的域
        r.append("[");
        do {
            Field[] fields = cl.getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);
            //获取所有域的名称和值
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    if (!r.toString().endsWith("[")) r.append(",");
                    r.append(field.getName()).append("=");
                    try {
                        Class t = field.getType();
                        Object val = field.get(obj);
                        if (t.isPrimitive()) r.append(val);
//                        else r += toString(val);
                        else r.append(val.toString());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            //获取父类
            cl = cl.getSuperclass();
        }
        while (cl != null);
        r.append("]");
        return r.toString();
    }
}
