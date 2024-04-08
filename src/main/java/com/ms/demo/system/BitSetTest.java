package com.ms.demo.system;

import java.util.BitSet;

public class BitSetTest {
    public static void main(String[] args) {
        BitSet bitSet = new BitSet(8);

        bitSet.set(1);
        bitSet.set(3);

        System.out.println(bitSet);
    }
}
