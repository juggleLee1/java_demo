package com.ms.demo.jvm;

public class ByteCode {
    private String userName;

    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static void main(String[] args) {
        new ByteCode();

    }
}
