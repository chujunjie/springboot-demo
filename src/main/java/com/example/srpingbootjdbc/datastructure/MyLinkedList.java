package com.example.srpingbootjdbc.datastructure;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 12:32 2019/5/4
 * @Modified By
 */
public class MyLinkedList<E> implements Iterable {

    private int size;
    /* 修改次数记录 */
    private int modCount;
    /* 头节点 */
    private Node<E> beginMarker;
    /* 尾节点 */
    private Node<E> endMarker;

    public MyLinkedList() {
        doClear(); // 初始化
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean add(E e) {
        add(size(), e);
        return true;
    }

    public void add(int index, E e) {
        addBefore(getNode(index, 0, size()), e);
    }

    public E get(int index) {
        return getNode(index).data;
    }

    public E set(int index, E newVal) {
        Node<E> oldNode = getNode(index);
        oldNode.data = newVal;
        return oldNode.data;
    }

    public E remove(int index) {
        return remove(getNode(index));
    }

    /**
     * 在指定节点node前添加一个元素
     *
     * @param node
     * @param e
     */
    private void addBefore(Node<E> node, E e) {
        Node<E> newNode = new Node<>(e, node.prev, node);
        newNode.prev.next = newNode; // 将前节点的next指针指向新节点
        node.prev = newNode; // 将后节点的prev指针指向新节点
        this.size++;
        this.modCount++;
    }

    /**
     * 删除节点
     *
     * @param node
     * @return
     */
    private <E> E remove(Node<E> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        this.size--;
        this.modCount++;
        return node.data;
    }

    /**
     * 根据索引获取节点
     *
     * @param index
     * @return
     */
    private Node<E> getNode(int index) {
        return getNode(index, 0, size() - 1);
    }

    /**
     * 从lower~upper范围内根据索引获取节点
     *
     * @param index
     * @param lower
     * @param upper
     * @return
     */
    private Node<E> getNode(int index, int lower, int upper) {
        if (index < lower || index > upper) {
            throw new IndexOutOfBoundsException();
        }

        Node<E> currentNode;

        // 如果在链表前半部分则从头遍历，反之从尾部开始遍历
        if (index < size() / 2) {
            currentNode = beginMarker.next;
            for (int i = 0; i < index; i++) {
                currentNode = currentNode.next;
            }
        } else {
            currentNode = endMarker;
            for (int i = size(); i > index; i--) {
                currentNode = currentNode.prev;
            }
        }

        return currentNode;
    }

    public void clear() {
        doClear();
    }

    private void doClear() {
        beginMarker = new Node<>(null, null, null);
        endMarker = new Node<>(null, beginMarker, null);
        beginMarker.next = endMarker;
        this.size = 0;
        modCount++;
    }

    private static class Node<E> {
        private E data;
        private Node<E> prev;
        private Node<E> next;

        public Node(E d, Node<E> p, Node<E> n) {
            this.data = d;
            this.prev = p;
            this.next = n;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedListIterator<>();
    }

    private class LinkedListIterator<E> implements Iterator<E> {

        private Node<E> current;
        private int exceptedModCount;
        private boolean validRemove = false;

        public LinkedListIterator() {
            this.current = (Node<E>) beginMarker.next;
            this.exceptedModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            return current != endMarker;
        }

        @Override
        public E next() {
            checkForModification();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E data = current.data;
            current = current.next;
            validRemove = true;
            return data;
        }

        @Override
        public void remove() {
            checkForModification();
            if (!validRemove) {
                throw new IllegalStateException();
            }
            MyLinkedList.this.remove(current.prev);
            exceptedModCount++;
            validRemove = false;
        }

        private void checkForModification() {
            if (exceptedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
