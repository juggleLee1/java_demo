package com.ms.demo.algorithm;

import java.nio.file.LinkOption;
import java.util.*;
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

    /***
     * 树的遍历之层序遍历
     * @param root
     * @return
     */
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res_list = new ArrayList<>();

        if (root == null) return res_list;

        Queue<TreeNode> queue = new LinkedList<>();

        queue.offer(root);
        TreeNode node;
        List<Integer> temp_list;
        int len, i;
        while (!queue.isEmpty()) {
            temp_list = new ArrayList<>();
            for (i = 0, len = queue.size(); i < len; i++) {
                node = queue.poll();
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
                temp_list.add(node.val);
            }
            res_list.add(temp_list);
        }

        return res_list;
    }

    /***
     * 树的遍历之先序遍历：非递归
     * @param root
     * @return
     */
    public List<Integer> preorderTraversal(TreeNode root) {
        if (root == null) return new ArrayList<>();
        List<Integer> list = new ArrayList<>();

        Deque<TreeNode> deque = new LinkedList<>();
        TreeNode node = root;
        while (!deque.isEmpty() || node != null) {
            while (node != null) {
                deque.offerFirst(node);
                list.add(node.val);
                node = node.left;
            }

            node = deque.pollFirst();
            node = node.right;
        }

        return list;
    }

    /***
     * 树的中序遍历：非递归
     * @return
     */
    public List<Integer> inorderTraversal(TreeNode root) {
        if (root == null) return new ArrayList<>();

        Deque<TreeNode> deque = new LinkedList<>();
        List<Integer> list = new ArrayList<>();
        TreeNode node = root;

        while (!deque.isEmpty() || node != null) {
            while (node != null) {
                deque.offerFirst(node);
                node = node.left;
            }

            node = deque.pollFirst();
            list.add(node.val);
            node = node.right;
        }

        return list;
    }


    /***
     * 树的后续遍历：非递归
     * @param root
     * @return
     */
    public List<Integer> postorderTraversal(TreeNode root) {
        if (root == null) return new ArrayList<>();

        Deque<TreeNode> deque1 = new LinkedList<>();
        Deque<TreeNode> deque2 = new LinkedList<>();

        deque1.offerFirst(root);
        TreeNode node;
        while (!deque1.isEmpty()) {
            node = deque1.pollFirst();
            deque2.offerFirst(node);
            if (node.left != null) deque1.offerFirst(node.left);
            if (node.right != null) deque1.offerFirst(node.right);
        }

        List<Integer> list = new ArrayList<>();
        while (!deque2.isEmpty()) {
            list.add(deque2.pollFirst().val);
        }

        return list;
    }

    /***
     * 冒泡排序
     * @param
     */
    public void bubbleSort(int[] list, Comparator<Integer> comparator) {
        int temp;
        boolean flag;
        for (int i = 0; i < list.length - 1; i++) {
            flag = true;
            for (int j = 0; j < list.length - i - 1; j++) {
                if (comparator.compare(list[j], list[j + 1]) > 0) {
                    temp = list[j];
                    list[j] = list[j + 1];
                    list[j + 1] = temp;
                    flag = false;
                }
            }
            if (flag) break;
        }
    }

    /**
     * 快速排序
     * @param list
     * @param start
     * @param end
     * @param comparator
     */
    public void quickSort(int[] list, int start, int end, Comparator<Integer> comparator) {
        if (start < end) {
            int pivot = list[start];
            int i = start, j = end;
            while (i < j) {
                while (i < j && comparator.compare(list[j], pivot) > 0) j--;
                if (i < j) list[i++] = list[j];

                while (i < j && comparator.compare(pivot, list[i]) >= 0) i++;
                if (i < j) list[j--] = list[i];
            }
            list[i] = pivot;

            quickSort(list, start, i - 1, comparator);
            quickSort(list, i + 1, end, comparator);
        }
    }


    /**
     * 堆排序
     * @param list
     * @param comparator
     */
    public void heapSort(int[] list, Comparator<Integer> comparator) {
        for (int i = (list.length >>> 1) - 1; i >= 0; i--) {
            heapJusify(list, i, list.length, comparator);
        }

        int temp;
        for (int i = list.length - 1; i > 0; i--) {
            temp = list[0];
            list[0] = list[i];
            list[i] = temp;
            heapJusify(list, 0, i, comparator);
        }
    }

    private void heapJusify(int[] list, int i, int len, Comparator<Integer> comparator) {
        int k = 2 * i + 1;
        int temp = list[i];
        while (k < len) {
            if (k + 1 < len && comparator.compare(list[k + 1], list[k]) > 0) k++;

            if (comparator.compare(list[k], temp) > 0) {
                list[i] = list[k];
                i = k;
                k = 2 * i + 1;
            } else {
                break;
            }
        }

        list[i] = temp;
    }

    public static void main(String[] args) {
        algorithmTest algorithmTest = new algorithmTest();
        int[] arr = {3, 10, 20, 20, 9, 80, 9, 20};

        algorithmTest.heapSort(arr, (num1, num2) -> {
            return num2 - num1;
        });

        System.out.println(Arrays.toString(arr));
    }
}

