package com.example.springbootdemo.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 双链表 + 哈希表 实现LRU算法，GET和PUT时间复杂度O(1)
 *
 * @author chujunjie
 * @date Create in 21:37 2020/7/21
 */
public class LRUCache {

    /**
     * 哈希表
     */
    private Map<String, LRUNode> map = new HashMap<>();

    /**
     * 首节点
     */
    private LRUNode head;

    /**
     * 尾节点
     */
    private LRUNode tail;

    /**
     * 容量
     */
    int capacity;

    public LRUCache(int capacity) {
        this.capacity = capacity;
    }

    /**
     * PUT
     *
     * @param key   key
     * @param value value
     */
    public void put(String key, Object value) {
        if (head == null) {
            head = new LRUNode(key, value);
            tail = head;
            map.put(key, head);
        }
        LRUNode node = map.get(key);
        if (node != null) {
            node.value = value;
            removeAndInsert(node);
        } else {
            LRUNode tmp = new LRUNode(key, value);
            if (map.size() >= capacity) {
                map.remove(tail.key);
                tail = tail.pre;
                tail.next = null;
            }
            map.put(key, tmp);
            tmp.next = head;
            head.pre = tmp;
            head = tmp;
        }
    }

    public Object get(String key) {
        LRUNode node = map.get(key);
        if (node != null) {
            removeAndInsert(node);
            return node.value;
        }
        return null;
    }

    private void removeAndInsert(LRUNode node) {
        if (node == head) {
            return;
        } else if (node == tail) {
            tail = node.pre;
            tail.next = null;
        } else {
            node.pre.next = node.next;
            node.next.pre = node.pre;
        }
        node.next = head;
        node.pre = null;
        head.pre = node;
        head = node;
    }

    /**
     * LRU节点
     */
    private static class LRUNode {
        String key;
        Object value;
        LRUNode next;
        LRUNode pre;

        public LRUNode(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }


}
