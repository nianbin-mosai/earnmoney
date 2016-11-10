package com.example;

import java.util.ArrayList;
import java.util.List;

public class MyClass {
    public static String test = "test";
    public static void main(String[] args){
//        int n=1;
//        int i=(n++);
//        System.out.println(++i);
//        System.out.println(limitStringLength(test,0));
//        System.out.println(limitStringLength(test,5));
//        System.out.println(HexUtil.MD5("123456"));
//        System.out.println(HexUtil.MD5("123456"));
//        System.out.println(HexUtil.MD5("123456"));
//        System.out.println(HexUtil.MD5("123456"));
        List<User> list = new ArrayList<>();
        User user = new User();
        user.id=1;
        User user2 = new User();
        user2.id=1;
        list.add(user);
        System.out.println(list.contains(user2));
    }
    public static String limitStringLength(String src,int count){
        return src.length()>count?src.substring(0,count):src;
    }
}
