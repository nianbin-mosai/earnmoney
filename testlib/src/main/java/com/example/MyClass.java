package com.example;

public class MyClass {
    public static String test = "test";
    public static void main(String[] args){
        int n=1;
        int i=(n++);
        System.out.println(++i);
        System.out.println(limitStringLength(test,0));
        System.out.println(limitStringLength(test,5));
    }
    public static String limitStringLength(String src,int count){
        return src.length()>count?src.substring(0,count):src;
    }
}
