package com.ms.demo.system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class ListTest {
    public static void main(String[] args) {
        LinkedList<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < 10000000; i++) {
            linkedList.add(i);
        }
        long start1 = System.currentTimeMillis();
        Iterator<Integer> iterator = linkedList.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        long end1 = System.currentTimeMillis();

        System.out.println("普通迭代器花费时间: " + (end1 - start1));

        long start2 = System.currentTimeMillis();
        for (Integer integer : linkedList) {

        }
        long end2 = System.currentTimeMillis();
        System.out.println("普通迭代器花费时间: " + (end2 - start2));

        long start3 = System.currentTimeMillis();
        ListIterator<Integer> listIterator = linkedList.listIterator(0);
        while (listIterator.hasNext()) {
            listIterator.next();
        }
        long end3 = System.currentTimeMillis();
        System.out.println("普通迭代器花费时间: " + (end3 - start3));
    }
}
