package com.example.srpingbootjdbc.datastructure;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 13:52 2019/5/12
 * @Modified By
 */
public class MyAvlTree<T extends Comparable<? super T>> {

    /* 允许不平衡的高度 */
    private static final int ALLOWED_IMBALANCE = 1;
    /* 根节点 */
    private AvlNode<T> root;

    public MyAvlTree() {
        this.root = null;
    }

    public MyAvlTree(T rootData) {
        this.root = new AvlNode<>(rootData);
    }

    public void clear() {
        this.root = null;
    }

    public boolean isEmpty() {
        return null == root;
    }

    private int height(AvlNode<T> node) {
        return null == node ? -1 : node.height;
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

    private AvlNode<T> findMin(AvlNode<T> node) {
        if (null == node) {
            return null;
        } else if (null == node.left) {
            return node;
        }

        return findMin(node.left);
    }

    private AvlNode<T> findMax(AvlNode<T> node) {
        if (null != node) {
            while (null != node.right) {
                node = node.right;
            }
        }
        return node;
    }

    private AvlNode<T> insert(T data, AvlNode<T> node) {
        if (null == node) {
            return new AvlNode<>(data, null, null);
        }

        int result = data.compareTo(node.data);

        if (result < 0) {
            node.left = insert(data, node.left);
        } else if (result > 0) {
            node.right = insert(data, node.right);
        }
        return balance(node);
    }

    private AvlNode<T> remove(T data, AvlNode<T> node) {
        if (null == node) {
            return node;
        }

        int result = data.compareTo(node.data);

        if (result < 0) {
            node.left = remove(data, node.left);
        } else if (result > 0) {
            node.right = remove(data, node.right);
        } else {
            if (null != node.left && null != node.right) {
                node.data = findMin(node.right).data;
                node.right = remove(node.data, node.right);
            } else {
                node = (null != node.left) ? node.left : node.right;
            }
        }
        return balance(node);
    }

    /**
     * 平衡树
     *
     * @param node
     * @return
     */
    private AvlNode<T> balance(AvlNode<T> node) {
        if (null == node) {
            return node;
        }

        if (height(node.left) - height(node.right) > ALLOWED_IMBALANCE) {
            node = height(node.left.left) >= height(node.left.right) ? rotateWithLeftChild(node) : doubleWithLeftChild(node);
        } else if (height(node.right) - height(node.left) > ALLOWED_IMBALANCE) {
            node = height(node.right.right) >= height(node.right.left) ? rotateWithRightChild(node) : doubleWithRightChild(node);
        }

        node.height = Math.max(height(node.left), height(node.right)) + 1;
        return node;
    }

    /**
     * 旋转左节点
     *
     * @param k2
     * @return
     */
    private AvlNode<T> rotateWithLeftChild(AvlNode<T> k2) {
        AvlNode<T> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(height(k1.left), k2.height) + 1;
        return k1;
    }

    /**
     * 旋转右节点
     *
     * @param k1
     * @return
     */
    private AvlNode<T> rotateWithRightChild(AvlNode<T> k1) {
        AvlNode<T> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
        k2.height = Math.max(height(k2.right), k1.height) + 1;
        return k2;
    }

    /**
     * 左-右双旋转
     *
     * @param k3
     * @return
     */
    private AvlNode<T> doubleWithLeftChild(AvlNode<T> k3) {
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    /**
     * 右-左双旋转
     *
     * @param k1
     * @return
     */
    private AvlNode<T> doubleWithRightChild(AvlNode<T> k1) {
        k1.right = rotateWithLeftChild(k1.right);
        return rotateWithRightChild(k1);
    }

    private static class AvlNode<T> {
        private T data;
        private AvlNode<T> left;
        private AvlNode<T> right;
        private int height;

        AvlNode(T data) {
            this(data, null, null);
        }

        AvlNode(T data, AvlNode<T> left, AvlNode<T> right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }
    }
}
