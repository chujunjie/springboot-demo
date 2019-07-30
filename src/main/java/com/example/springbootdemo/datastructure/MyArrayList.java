package com.example.springbootdemo.datastructure;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 21:43 2019/5/3
 * @Modified By
 */
public class MyArrayList<E> {

    private static final int DEFAULT_CAPACITY = 5;

    private int size;

    public E[] arr;

    @SuppressWarnings("unchecked")
    public MyArrayList() {
        arr = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public void clear() {
        doClear();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void trimToSize() {
        ensureCapacity(size());
    }

    public boolean add(E t) {
        if (arr.length == size()) {
            ensureCapacity(size() * 2 + 1);
        }

        arr[size++] = t;
        return true;
    }

    public boolean add(int index, E t) {
        checkIndex(index);

        if (arr.length == size()) {
            ensureCapacity(size() * 2 + 1);
        }

        System.arraycopy(arr, index, arr, index + 1,
                size - index);
        arr[index] = t;
        size++;
        return true;
    }

    public E get(int index) {
        checkIndex(index);

        return arr[index];
    }

    public E set(int index, E t) {
        checkIndex(index);

        E old = arr[index];
        arr[index] = t;
        return old;
    }

    public E remove(int index) {
        checkIndex(index);
        E removedItem = arr[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(arr, index + 1, arr, index, numMoved);
        }
        arr[--size] = null;
        return removedItem;
    }

    private void doClear() {
        this.size = DEFAULT_CAPACITY;
        ensureCapacity(DEFAULT_CAPACITY);
    }

    private void ensureCapacity(int newCapacity) {
        if (newCapacity < size) {
            return;
        }
        arr = Arrays.copyOf(arr, newCapacity);
    }

    private void checkIndex(int index) {
        if (index < 0 || index > size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public Iterator<E> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<E> {

        private int current;

        @Override
        public boolean hasNext() {
            return current < size();
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return arr[current++];
        }

        @Override
        public void remove() {
            MyArrayList.this.remove(--current);
        }
    }
}
