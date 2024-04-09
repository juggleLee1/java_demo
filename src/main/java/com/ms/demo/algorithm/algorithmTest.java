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
}

