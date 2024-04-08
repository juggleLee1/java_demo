package com.ms.demo.system;

import java.util.ArrayList;

public class ListTest {
    public static void main(String[] args) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);

        arrayList.replaceAll(n -> n * n);

        System.out.println(arrayList);
    }
}
