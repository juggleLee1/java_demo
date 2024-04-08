package com.ms.demo.system;

import java.util.ArrayList;

public class IteratorTest {
    public static void main(String[] args) {
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(1);
        arr.add(2);

        for (Integer integer : arr) {
            System.out.println(integer);
        }
    }
}
