package com.ms.demo.jvm;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.CountDownLatch;

/**
 * ClassName: ObjectHeadTest
 * Package: com.ms.demo.jvm
 * Description:
 *
 * @author Lee
 * @version 1.0
 * @create 2024/4/10 20:45
 */
public class ObjectHeadTest {
    public static void main(String[] args) {
        System.out.println(ClassLayout.parseInstance(new Object()).toPrintable());

        System.out.println();

        System.out.println(ClassLayout.parseInstance(new int[2]).toPrintable());

        System.out.println();

        System.out.println(ClassLayout.parseInstance(new A()).toPrintable());
    }

    public static class A {
        private String name;
        private int age;
        private byte p;
        private Object o;
    }
}
