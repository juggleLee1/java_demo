package com.ms.demo.jvm;

public class JVMTest {
    public static void main(String[] args) {
        String property = System.getProperty("java.ext.dirs");
        String[] split = property.split(";");
        for (String s : split) {
            System.out.println(s);
        }
    }
}
