package com.ms.demo.jvm;

public class ByteCode {
    private String userName;

    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        test();
    }

    public void test(){
        System.out.println(111);
    }

    public static void main(String[] args) {

    }

}
