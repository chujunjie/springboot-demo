package com.example.srpingbootjdbc.datastructure;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 22:07 2019/5/7
 * @Modified By
 */
public class MyBinarySearchTree<T extends Comparable<? super T>> {

    /* 根节点 */
    private BinaryNode<T> root;

    public MyBinarySearchTree() {
        this.root = null;
    }

    public MyBinarySearchTree(T rootData) {
        this.root = new BinaryNode<>(rootData);
    }

    public void clear() {
        this.root = null;
    }

    public boolean isEmpty() {
        return null == root;
    }

    /**
     * 查找二叉树中是否存在数据data
     *
     * @param data
     * @return
     */
    public boolean contains(T data) {
        return contains(data, root);
    }

    /**
     * 取最小值
     *
     * @return
     */
    public T findMin() {
        if (isEmpty()) {
            throw new RuntimeException("Empty tree!");
        }
        return findMin(root).data;
    }

    /**
     * 取最大值
     *
     * @return
     */
    public T findMax() {
        if (isEmpty()) {
            throw new RuntimeException("Empty tree!");
        }
        return findMax(root).data;
    }

    /**
     * 插入数据
     *
     * @param data
     */
    public void insert(T data) {
        root = insert(data, root);
    }

    /**
     * 删除数据
     *
     * @param data
     */
    public void remove(T data) {
        root = remove(data, root);
    }

    private boolean contains(T data, BinaryNode<T> node) {
        if (null == data) {
            return false;
        }

        int result = data.compareTo(node.data);

        if (result < 0) {
            return contains(data, node.left);
        } else if (result > 0) {
            return contains(data, node.right);
        } else {
            return true; // Match
        }
    }

    private BinaryNode<T> findMin(BinaryNode<T> node) {
        if (null == node) {
            return null;
        } else if (null == node.left) {
            return node;
        }

        return findMin(node.left);
    }

    private BinaryNode<T> findMax(BinaryNode<T> node) {
        if (null != node) {
            while (null != node.right) {
                node = node.right;
            }
        }
        return node;
    }

    private BinaryNode<T> insert(T data, BinaryNode<T> node) {
        if (null == node) {
            return new BinaryNode<>(data, null, null);
        }
        int result = data.compareTo(node.data);
        if (result < 0) {
            node.left = insert(data, node.left);
        } else if (result > 0) {
            node.right = insert(data, node.right);
        }
        return node;
    }

    private BinaryNode<T> remove(T data, BinaryNode<T> node) {
        if (null == node) {
            throw new RuntimeException("Item not found!");
        }

        int result = data.compareTo(node.data);

        if (result < 0) {
            node.left = remove(data, node.left);
        } else if (result > 0) {
            node.right = remove(data, node.right);
        } else {
            // 两个子节点，从右子节点中选出最小值替代删除的节点
            // 这种算法使得左子树比右子树深度深，造成期望深度大于O(logN)
            if (null != node.left && null != node.right) {
                node.data = findMin(node.right).data;
                node.right = remove(node.data, node.right);
            } else {
                node = (null != node.left) ? node.left : node.right;
            }
        }
        return node;
    }

    /**
     * 节点
     *
     * @param <T>
     */
    private static class BinaryNode<T> {

        private T data;
        private BinaryNode<T> left;
        private BinaryNode<T> right;

        public BinaryNode(T t) {
            this(t, null, null);
        }

        public BinaryNode(T t, BinaryNode<T> lt, BinaryNode<T> rt) {
            this.data = t;
            this.left = lt;
            this.right = rt;
        }
    }
}
