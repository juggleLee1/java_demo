package com.ms.demo.system;

import java.util.Arrays;
import java.util.Scanner;

public class StringTest {

    public static void main(String[] args) {
        int[] srcArr = {1, 2, 3, 4, 5};

//        System.arraycopy(srcArr, 1, srcArr, 3, srcArr.length - 3);

//        int[] ints = Arrays.copyOf(srcArr, 10); // [1, 2, 3, 4, 5, 0, 0, 0, 0, 0]

        int[] ints = Arrays.copyOfRange(srcArr, 1, 4);  // [2, 3, 4]

        System.out.println(Arrays.toString(ints));
    }

}
