package com.ms.demo.algorithm;

import java.util.BitSet;
import java.util.function.Predicate;

/**
 * ClassName: algorithmTest
 * Package: com.ms.demo.algorithm
 * Description:
 *
 * @author Lee
 * @version 1.0
 * @create 2024/4/8 19:22
 */
public class algorithmTest {
    public static void main(String[] args) {

    }

    // lc 27题
    public static int removeElement(int[] nums, int val) {
        return removeIf(nums, num -> num == val);
    }

    public static int removeIf(int[] nums, Predicate<Integer> predicate) {
        BitSet bitSet = new BitSet(nums.length);
        int remove_num = 0;
        for (int i = 0; i < nums.length; i++) {
            if (predicate.test(nums[i])) {
                remove_num++;
                bitSet.set(i);
            }
        }

        if (remove_num == 0) return nums.length;

        for (int i = 0, j = 0; i < nums.length && j < nums.length - remove_num; i++, j++) {
            i = bitSet.nextClearBit(i);
            nums[j] = nums[i];
        }

        return nums.length - remove_num;
    }
}
