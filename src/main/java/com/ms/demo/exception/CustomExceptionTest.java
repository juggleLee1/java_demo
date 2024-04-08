package com.ms.demo.exception;

public class CustomExceptionTest {
    public static void main(String[] args) {
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            throw new MyCustomException("不能被0除");
        }
    }
}
